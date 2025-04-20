package com.waste.management.controller;

import com.waste.management.dao.ComplaintDAO;
import com.waste.management.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/complaint")
public class AdminComplaintServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ComplaintDAO complaintDAO;
    
    public void init() {
        complaintDAO = new ComplaintDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // This method is used for operations like resolve, delete
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get operation and complaint ID
        String operation = request.getParameter("operation");
        String complaintIdStr = request.getParameter("id");
        
        if (operation == null || complaintIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        
        try {
            int complaintId = Integer.parseInt(complaintIdStr);
            
            boolean success = false;
            String successMessage = "";
            
            // Process the operation
            switch (operation) {
                case "resolve":
                    success = complaintDAO.resolveComplaint(complaintId);
                    successMessage = "Complaint resolved successfully!";
                    break;
                case "delete":
                    success = complaintDAO.deleteComplaint(complaintId);
                    successMessage = "Complaint deleted successfully!";
                    break;
                case "inprogress":
                    success = complaintDAO.updateComplaintStatus(complaintId, "IN_PROGRESS");
                    successMessage = "Complaint status updated to In Progress!";
                    break;
                default:
                    // Invalid operation
                    break;
            }
            
            if (success) {
                session.setAttribute("successMessage", successMessage);
            } else {
                session.setAttribute("errorMessage", "Operation failed. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid complaint ID.");
        }
        
        // Redirect back to admin dashboard
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
} 
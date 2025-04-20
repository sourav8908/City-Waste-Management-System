package com.waste.management.controller;

import com.waste.management.dao.ComplaintDAO;
import com.waste.management.model.Complaint;
import com.waste.management.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet("/user/complaint")
public class ComplaintServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ComplaintDAO complaintDAO;
    
    public void init() {
        complaintDAO = new ComplaintDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user has user role
        User user = (User) session.getAttribute("user");
        if (!"user".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Forward to complaint form page
        request.getRequestDispatcher("/user/complaint-form.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        // Get form parameters
        String complaintType = request.getParameter("complaintType");
        String description = request.getParameter("description");
        
        // Validate inputs
        if (complaintType == null || complaintType.trim().isEmpty() ||
            description == null || description.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/user/complaint-form.jsp").forward(request, response);
            return;
        }
        
        // Create new complaint
        Complaint complaint = new Complaint();
        complaint.setUserId(user.getId());
        complaint.setComplaintType(complaintType);
        complaint.setDescription(description);
        complaint.setStatus("PENDING");
        complaint.setCreatedDate(new Date());
        
        // Save complaint
        if (complaintDAO.addComplaint(complaint)) {
            // Complaint added successfully
            session.setAttribute("successMessage", "Complaint submitted successfully!");
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        } else {
            // Failed to add complaint
            request.setAttribute("errorMessage", "Failed to submit complaint. Please try again.");
            request.getRequestDispatcher("/user/complaint-form.jsp").forward(request, response);
        }
    }
} 
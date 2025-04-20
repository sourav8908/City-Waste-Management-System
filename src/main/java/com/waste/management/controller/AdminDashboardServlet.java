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
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
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
        
        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get all complaints with user details
        List<Object[]> complaintDetails = complaintDAO.getComplaintsWithUserDetails();
        request.setAttribute("complaintDetails", complaintDetails);
        
        // Forward to admin dashboard page
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
} 
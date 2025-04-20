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
import java.util.List;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
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
        
        // Get user's complaints
        List<Complaint> complaints = complaintDAO.getComplaintsByUserId(user.getId());
        request.setAttribute("complaints", complaints);
        
        // Forward to user dashboard page
        request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
    }
} 
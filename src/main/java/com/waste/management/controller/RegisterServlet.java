package com.waste.management.controller;

import com.waste.management.dao.UserDAO;
import com.waste.management.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    public void init() {
        userDAO = new UserDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        
        // Validate user inputs
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All required fields must be filled out.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Check if username already exists
        if (userDAO.isUsernameExists(username)) {
            request.setAttribute("errorMessage", "Username already exists. Please choose a different one.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setRole("user"); // Default role is user
        
        // Register user
        if (userDAO.registerUser(user)) {
            // Registration successful
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Registration successful! Please login.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Registration failed
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
} 
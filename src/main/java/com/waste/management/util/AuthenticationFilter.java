package com.waste.management.util;

import com.waste.management.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*", "/admin/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code here
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get session
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        
        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        if (isLoggedIn) {
            User user = (User) session.getAttribute("user");
            
            // Check if user is trying to access admin pages but is not an admin
            if (requestURI.contains("/admin/") && !"admin".equals(user.getRole())) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/dashboard");
                return;
            }
            
            // Check if admin is trying to access user pages
            if (requestURI.contains("/user/") && "admin".equals(user.getRole())) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/dashboard");
                return;
            }
            
            // User is authenticated and authorized, continue with the request
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code here
    }
} 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.waste.management.model.User, com.waste.management.model.Complaint, java.util.List, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - City Waste Management</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <div class="nav-container">
            <a href="dashboard" class="nav-brand">City Waste Management</a>
            <div class="nav-links">
                <a href="../logout" class="nav-link">Logout</a>
            </div>
        </div>
    </div>

    <div class="container">
        <% 
            User user = (User) session.getAttribute("user");
            if (user == null || !"admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        %>
        
        <h1>Admin Dashboard</h1>
        
        <!-- Success Message -->
        <% if (session.getAttribute("successMessage") != null) { %>
            <div class="message success-message">
                <span><%= session.getAttribute("successMessage") %></span>
                <button class="close-btn" onclick="this.parentElement.style.display='none';">&times;</button>
            </div>
            <% session.removeAttribute("successMessage"); %>
        <% } %>
        
        <!-- Error Message -->
        <% if (session.getAttribute("errorMessage") != null) { %>
            <div class="message error-message">
                <span><%= session.getAttribute("errorMessage") %></span>
                <button class="close-btn" onclick="this.parentElement.style.display='none';">&times;</button>
            </div>
            <% session.removeAttribute("errorMessage"); %>
        <% } %>
        
        <div class="card">
            <h2>All Complaints</h2>
            
            <% 
                List<Object[]> complaintDetails = (List<Object[]>) request.getAttribute("complaintDetails");
                if (complaintDetails != null && !complaintDetails.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            %>
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Created Date</th>
                        <th>User Info</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Object[] detail : complaintDetails) { 
                        Complaint complaint = (Complaint) detail[0];
                        User complaintUser = (User) detail[1];
                    %>
                    <tr>
                        <td><%= complaint.getId() %></td>
                        <td><%= complaint.getComplaintType() %></td>
                        <td><%= complaint.getDescription() %></td>
                        <td>
                            <% if ("PENDING".equals(complaint.getStatus())) { %>
                                <span class="status-tag status-pending">Pending</span>
                            <% } else if ("IN_PROGRESS".equals(complaint.getStatus())) { %>
                                <span class="status-tag status-in-progress">In Progress</span>
                            <% } else if ("RESOLVED".equals(complaint.getStatus())) { %>
                                <span class="status-tag status-resolved">Resolved</span>
                            <% } %>
                        </td>
                        <td><%= dateFormat.format(complaint.getCreatedDate()) %></td>
                        <td>
                            <strong>Name:</strong> <%= complaintUser.getName() %><br>
                            <strong>Address:</strong> <%= complaintUser.getAddress() %><br>
                            <strong>Phone:</strong> <%= complaintUser.getPhoneNumber() %><br>
                            <strong>Email:</strong> <%= complaintUser.getEmail() %>
                        </td>
                        <td>
                            <% if (!"RESOLVED".equals(complaint.getStatus())) { %>
                                <% if ("PENDING".equals(complaint.getStatus())) { %>
                                    <a href="complaint?operation=inprogress&id=<%= complaint.getId() %>" 
                                       class="btn primary-btn" style="font-size: 12px; padding: 5px 10px; margin-bottom: 5px; display: block;">
                                        Mark In Progress
                                    </a>
                                <% } %>
                                <a href="complaint?operation=resolve&id=<%= complaint.getId() %>" 
                                   class="btn secondary-btn" style="font-size: 12px; padding: 5px 10px; margin-bottom: 5px; display: block;">
                                    Resolve
                                </a>
                            <% } %>
                            <a href="complaint?operation=delete&id=<%= complaint.getId() %>" 
                               class="btn danger-btn" style="font-size: 12px; padding: 5px 10px; display: block;"
                               onclick="return confirm('Are you sure you want to delete this complaint?');">
                                Delete
                            </a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } else { %>
            <div style="margin-top: 20px; text-align: center;">
                <p>There are no complaints in the system.</p>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html> 
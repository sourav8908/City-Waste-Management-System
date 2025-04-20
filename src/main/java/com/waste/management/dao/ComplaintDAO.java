package com.waste.management.dao;

import com.waste.management.model.Complaint;
import com.waste.management.model.User;
import com.waste.management.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComplaintDAO {
    
    // Add new complaint
    public boolean addComplaint(Complaint complaint) {
        String query = "INSERT INTO complaints (user_id, complaint_type, description, status, created_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, complaint.getUserId());
            pstmt.setString(2, complaint.getComplaintType());
            pstmt.setString(3, complaint.getDescription());
            pstmt.setString(4, complaint.getStatus());
            pstmt.setTimestamp(5, new Timestamp(complaint.getCreatedDate().getTime()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get complaint by ID
    public Complaint getComplaintById(int complaintId) {
        String query = "SELECT * FROM complaints WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, complaintId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractComplaintFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Get all complaints
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaints ORDER BY created_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // Get complaints by user ID
    public List<Complaint> getComplaintsByUserId(int userId) {
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaints WHERE user_id = ? ORDER BY created_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(extractComplaintFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // Update complaint status
    public boolean updateComplaintStatus(int complaintId, String status) {
        String query = "UPDATE complaints SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, complaintId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete complaint
    public boolean deleteComplaint(int complaintId) {
        String query = "DELETE FROM complaints WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, complaintId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Resolve complaint (update status to RESOLVED and set resolved date)
    public boolean resolveComplaint(int complaintId) {
        String query = "UPDATE complaints SET status = 'RESOLVED', resolved_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setInt(2, complaintId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get complaints with user details (for admin view)
    public List<Object[]> getComplaintsWithUserDetails() {
        List<Object[]> complaintDetails = new ArrayList<>();
        String query = "SELECT c.*, u.name, u.address, u.phone_number, u.email FROM complaints c " +
                       "JOIN users u ON c.user_id = u.id ORDER BY c.created_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Complaint complaint = extractComplaintFromResultSet(rs);
                
                // Create a temporary user object with just the details needed
                User user = new User();
                user.setName(rs.getString("name"));
                user.setAddress(rs.getString("address"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                
                complaintDetails.add(new Object[]{complaint, user});
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaintDetails;
    }
    
    // Helper method to extract complaint from ResultSet
    private Complaint extractComplaintFromResultSet(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setUserId(rs.getInt("user_id"));
        complaint.setComplaintType(rs.getString("complaint_type"));
        complaint.setDescription(rs.getString("description"));
        complaint.setStatus(rs.getString("status"));
        complaint.setCreatedDate(new Date(rs.getTimestamp("created_date").getTime()));
        
        Timestamp resolvedDate = rs.getTimestamp("resolved_date");
        if (resolvedDate != null) {
            complaint.setResolvedDate(new Date(resolvedDate.getTime()));
        }
        
        return complaint;
    }
} 
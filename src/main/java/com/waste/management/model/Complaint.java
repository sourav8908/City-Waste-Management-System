package com.waste.management.model;

import java.util.Date;

public class Complaint {
    private int id;
    private int userId;
    private String complaintType; // "NEW_DUSTBIN", "DUSTBIN_OVERFLOW", "DUSTBIN_DAMAGED"
    private String description;
    private String status; // "PENDING", "IN_PROGRESS", "RESOLVED"
    private Date createdDate;
    private Date resolvedDate;

    // Default constructor
    public Complaint() {
    }

    // Parameterized constructor
    public Complaint(int id, int userId, String complaintType, String description, String status, Date createdDate, Date resolvedDate) {
        this.id = id;
        this.userId = userId;
        this.complaintType = complaintType;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.resolvedDate = resolvedDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
} 
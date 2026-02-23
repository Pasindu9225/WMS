package com.example.wms.security;

public class LoginRequest {
    private String username;
    private String groupId;
    private String companyId;

    // Default constructor is REQUIRED for JSON mapping
    public LoginRequest() {}

    // Manual Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}
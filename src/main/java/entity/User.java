package com.demo.secondhand.entity;

import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String contact;
    private Date createTime;
    private String role;
    private Integer verified;   // 新增

    // 所有字段的 getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getVerified() { return verified; }
    public void setVerified(Integer verified) { this.verified = verified; }
}
package com.btlon.bantraicay.models;

public class QuanTriVien {
    private String id;
    private String username;
    private String password;

    // Constructors
    public QuanTriVien() {
        // Default constructor required for calls to DataSnapshot.getValue(QuanTriVien.class)
    }

    public QuanTriVien(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

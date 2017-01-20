package com.dwwen.model;

import java.util.List;

/**
 * Created by abdulaziz on 12/15/2014.
 */
public class RegistrationError {
    private  List<String> username;
    private  List<String> password;
    private  List<String> email;
    private String detail;

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

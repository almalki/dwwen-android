package com.dwwen.model;

import java.util.List;

/**
 * Created by abdulaziz on 12/19/2014.
 */
public class ChangePasswordError {
    private List<String> oldPassword;
    private  List<String> newPassword;
    private String detail;

    public List<String> getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(List<String> oldPassword) {
        this.oldPassword = oldPassword;
    }

    public List<String> getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(List<String> newPassword) {
        this.newPassword = newPassword;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

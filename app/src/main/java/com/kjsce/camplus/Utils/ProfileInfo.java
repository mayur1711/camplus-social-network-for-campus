package com.kjsce.camplus.Utils;

import android.util.Log;

/**
 * Created by Mayur on 12-03-2018.
 */

public class ProfileInfo {

    private static final String TAG = "ProfileInfo";

    private static ProfileInfo instance;
    private int userId;
    private String name, email, username, contactNo, year, department;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getYear() {
        return year;
    }

    public String getDepartment() {
        return department;
    }

    public static synchronized ProfileInfo getInstance() {
        if (instance == null) {
            instance = new ProfileInfo();
        }
        return instance;
    }
}

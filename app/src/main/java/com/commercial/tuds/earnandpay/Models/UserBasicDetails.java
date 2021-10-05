package com.commercial.tuds.earnandpay.Models;

public class UserBasicDetails {

    private String username;
    private String mobile;
    private String uid;


    public UserBasicDetails(String username, String mobile, String uid) {
        this.username = username;
        this.mobile = mobile;
        this.uid = uid;
    }

    public UserBasicDetails() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserBasicDetails{" +
                "username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}

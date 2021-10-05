package com.commercial.tuds.earnandpay.Models;


import java.util.ArrayList;

public class UserDetails {

    private String username;
    private String email;
    private String mobile;
    private Boolean loginStatus;
    private Address address;
    private String profile_url;
    private ArrayList<Notification> notificationList;
    private float fixedBalance;
    private String joinedOn;
    private String fixedBalanceDate;
    private float loanEligibilityAmount;
    private Boolean kycStatus;
    private float totalLoanCredited;
    private float loanPaidAmount;
    private float dueLoanAmount;
    private String loanEligibilityStatus;
    private int numberOfCards;



    public UserDetails() {
    }

    public UserDetails(String username, String email, String mobile, Boolean loginStatus, Address address, String profile_url, ArrayList<Notification> notificationList, float fixedBalance, String joinedOn, String fixedBalanceDate, float loanEligibilityAmount, Boolean kycStatus, float totalLoanCredited, float loanPaidAmount, float dueLoanAmount,String loanEligibilityStatus, int numberOfCards) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.loginStatus = loginStatus;
        this.address = address;
        this.profile_url = profile_url;
        this.notificationList = notificationList;
        this.fixedBalance = fixedBalance;
        this.joinedOn = joinedOn;
        this.fixedBalanceDate = fixedBalanceDate;
        this.loanEligibilityAmount = loanEligibilityAmount;
        this.kycStatus = kycStatus;
        this.totalLoanCredited = totalLoanCredited;
        this.loanPaidAmount = loanPaidAmount;
        this.dueLoanAmount = dueLoanAmount;
        this.loanEligibilityStatus = loanEligibilityStatus;
        this.numberOfCards = numberOfCards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Boolean getLoggedIn() {
        return loginStatus;
    }

    public void setLoggedIn(Boolean loggedIn) {
        loginStatus = loggedIn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public float getFixedBalance() {
        return fixedBalance;
    }

    public void setFixedBalance(float fixedBalance) {
        this.fixedBalance = fixedBalance;
    }

    public String getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(String joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getFixedBalanceDate() {
        return fixedBalanceDate;
    }

    public void setFixedBalanceDate(String fixedBalanceDate) {
        this.fixedBalanceDate = fixedBalanceDate;
    }

    public float getLoanEligibilityAmount() {
        return loanEligibilityAmount;
    }

    public void setLoanEligibilityAmount(float loanEligibilityAmount) {
        this.loanEligibilityAmount = loanEligibilityAmount;
    }

    public Boolean getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(Boolean kycStatus) {
        this.kycStatus = kycStatus;
    }

    public float getTotalLoanCredited() {
        return totalLoanCredited;
    }

    public void setTotalLoanCredited(float totalLoanCredited) {
        this.totalLoanCredited = totalLoanCredited;
    }

    public float getLoanPaidAmount() {
        return loanPaidAmount;
    }

    public void setLoanPaidAmount(float loanPaidAmount) {
        this.loanPaidAmount = loanPaidAmount;
    }

    public float getDueLoanAmount() {
        return dueLoanAmount;
    }

    public void setDueLoanAmount(float dueLoanAmount) {
        this.dueLoanAmount = dueLoanAmount;
    }

    public String getLoanEligibilityStatus() {
        return loanEligibilityStatus;
    }

    public void setLoanEligibilityStatus(String loanEligibilityStatus) {
        this.loanEligibilityStatus = loanEligibilityStatus;
    }
    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }
    @Override
    public String toString() {
        return "UserDetails{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", loginStatus=" + loginStatus +
                ", address=" + address +
                ", profile_url='" + profile_url + '\'' +
                ", notificationList=" + notificationList +
                ", fixedBalance=" + fixedBalance +
                ", joinedOn='" + joinedOn + '\'' +
                ", fixedBalanceDate='" + fixedBalanceDate + '\'' +
                ", loanEligibilityAmount=" + loanEligibilityAmount +
                ", kycStatus=" + kycStatus +
                ", totalLoanCredited=" + totalLoanCredited +
                ", loanPaidAmount=" + loanPaidAmount +
                ", dueLoanAmount=" + dueLoanAmount +
                ", loanEligibilityStatus='" + loanEligibilityStatus + '\'' +
                ", numberOfCards=" + numberOfCards +
                '}';
    }
}

package com.commercial.tuds.earnandpay.Models;

public class AccountDetails {

    private String accountNumber;
    private String accountName;
    private String IFSCCode;

    public AccountDetails(String accountNumber, String accountName, String IFSCCode) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.IFSCCode = IFSCCode;
    }

    public AccountDetails() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", IFSCCode='" + IFSCCode + '\'' +
                '}';
    }
}

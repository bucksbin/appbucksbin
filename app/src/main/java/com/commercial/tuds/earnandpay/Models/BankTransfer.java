package com.commercial.tuds.earnandpay.Models;

public class BankTransfer {

    private String accountNumber;
    private String holderName;
    private String ifscCode;
    private String bankTransferID;
    private float amount;

    public BankTransfer(String accountNumber, String holderName, String ifscCode, float amount) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.ifscCode = ifscCode;
        this.amount = amount;
    }

    public String getBankTransferID() {
        return bankTransferID;
    }

    public void setBankTransferID(String bankTransferID) {
        this.bankTransferID = bankTransferID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BankTransfer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", holderName='" + holderName + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", amount=" + amount +
                '}';
    }
}

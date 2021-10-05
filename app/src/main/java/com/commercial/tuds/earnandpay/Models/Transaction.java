package com.commercial.tuds.earnandpay.Models;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String transactionType;
    private float transactionAmount;
    private Boolean isSuccessful;
    private String transactionAction;
    private String accountNumber;
    private String transactionReferenceNumber;
    private String bankReferenceNumber;
    private String transactionTime;
    private String transactionRemark;
    private float closingBalance;


    public Transaction() {
    }

    public Transaction(String transactionType, float transactionAmount, Boolean isSuccessful, String transactionAction, String accountNumber, String transactionReferenceNumber, String bankReferenceNumber, String transactionTime,String transactionRemark,float closingBalance) {
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.isSuccessful = isSuccessful;
        this.transactionAction = transactionAction;
        this.accountNumber = accountNumber;
        this.transactionReferenceNumber = transactionReferenceNumber;
        this.bankReferenceNumber = bankReferenceNumber;
        this.transactionTime = transactionTime;
        this.transactionRemark = transactionRemark;
        this.closingBalance = closingBalance;
    }

    public float getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(float closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getTransactionAction() {
        return transactionAction;
    }

    public void setTransactionAction(String transactionAction) {
        this.transactionAction = transactionAction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getBankReferenceNumber() {
        return bankReferenceNumber;
    }

    public void setBankReferenceNumber(String bankReferenceNumber) {
        this.bankReferenceNumber = bankReferenceNumber;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionRemark() {
        return transactionRemark;
    }

    public void setTransactionRemark(String transactionRemark) {
        this.transactionRemark = transactionRemark;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType='" + transactionType + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", isSuccessful=" + isSuccessful +
                ", transactionAction='" + transactionAction + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionReferenceNumber='" + transactionReferenceNumber + '\'' +
                ", bankReferenceNumber='" + bankReferenceNumber + '\'' +
                ", transactionTime='" + transactionTime + '\'' +
                '}';
    }
}


package com.commercial.tuds.earnandpay.Models;

public class LoanApplication {

    private String username;
    private String fatherName;
    private String motherName;
    private Address address;
    private String aadharNumber;
    private String occupationType;
    private String salarySlipURL;
    private String gasConnectionNumber;
    private String gasSubsidyAccountNumber;
    private String gasSubsidyBank;
    private AccountDetails accountDetails;


    public LoanApplication() {
    }

    public LoanApplication(String username, String fatherName, String motherName, Address address, String aadharNumber, String occupationType, String salarySlipURL, String gasConnectionNumber, String gasSubsidyAccountNumber, String gasSubsidyBank,AccountDetails accountDetails) {
        this.username = username;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.address = address;
        this.aadharNumber = aadharNumber;
        this.occupationType = occupationType;
        this.salarySlipURL = salarySlipURL;
        this.gasConnectionNumber = gasConnectionNumber;
        this.gasSubsidyAccountNumber = gasSubsidyAccountNumber;
        this.gasSubsidyBank = gasSubsidyBank;
        this.accountDetails = accountDetails;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(String occupationType) {
        this.occupationType = occupationType;
    }

    public String getSalarySlipURL() {
        return salarySlipURL;
    }

    public void setSalarySlipURL(String salarySlipURL) {
        this.salarySlipURL = salarySlipURL;
    }

    public String getGasConnectionNumber() {
        return gasConnectionNumber;
    }

    public void setGasConnectionNumber(String gasConnectionNumber) {
        this.gasConnectionNumber = gasConnectionNumber;
    }

    public String getGasSubsidyAccountNumber() {
        return gasSubsidyAccountNumber;
    }

    public void setGasSubsidyAccountNumber(String gasSubsidyAccountNumber) {
        this.gasSubsidyAccountNumber = gasSubsidyAccountNumber;
    }

    public String getGasSubsidyBank() {
        return gasSubsidyBank;
    }

    public void setGasSubsidyBank(String gasSubsidyBank) {
        this.gasSubsidyBank = gasSubsidyBank;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "username='" + username + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", address=" + address +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", occupationType='" + occupationType + '\'' +
                ", salarySlipURL='" + salarySlipURL + '\'' +
                ", gasConnectionNumber='" + gasConnectionNumber + '\'' +
                ", gasSubsidyAccountNumber='" + gasSubsidyAccountNumber + '\'' +
                ", gasSubsidyBank='" + gasSubsidyBank + '\'' +
                ", accountDetails=" + accountDetails +
                '}';
    }
}

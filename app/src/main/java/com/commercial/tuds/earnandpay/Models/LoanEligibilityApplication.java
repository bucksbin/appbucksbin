package com.commercial.tuds.earnandpay.Models;

public class LoanEligibilityApplication {

    private String name;
    private String organisationName;
    private String employeeStatus;
    private String annualIncome;
    private String panNumber;

    public LoanEligibilityApplication() {
    }

    public LoanEligibilityApplication(String name, String organisationName, String employeeStatus, String annualIncome, String panNumber) {
        this.name = name;
        this.organisationName = organisationName;
        this.employeeStatus = employeeStatus;
        this.annualIncome = annualIncome;
        this.panNumber = panNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    @Override
    public String toString() {
        return "LoanEligibilityApplication{" +
                "name='" + name + '\'' +
                ", organisationName='" + organisationName + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", annualIncome='" + annualIncome + '\'' +
                ", panNumber='" + panNumber + '\'' +
                '}';
    }
}

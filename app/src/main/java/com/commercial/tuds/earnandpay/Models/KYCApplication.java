package com.commercial.tuds.earnandpay.Models;

public class KYCApplication {

    private String name;
    private String aadharNo;
    private String panNo;

    public KYCApplication(String name, String aadharNo, String panNo) {
        this.name = name;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
    }

    public KYCApplication() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    @Override
    public String toString() {
        return "KYCApplication{" +
                "name='" + name + '\'' +
                ", aadharNo='" + aadharNo + '\'' +
                ", panNo='" + panNo + '\'' +
                '}';
    }
}

package com.commercial.tuds.earnandpay.Models;

import java.util.ArrayList;

public class PayToAllPojo {


    private String category;
    private String projectID;
    private String userID;
    private ArrayList<String> topicName;
    private String fromLimit;
    private String toLimit;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<String> getTopicName() {
        return topicName;
    }

    public void setTopicName(ArrayList<String> topicName) {
        this.topicName = topicName;
    }


    public String getFromLimit() {
        return fromLimit;
    }

    public void setFromLimit(String fromLimit) {
        this.fromLimit = fromLimit;
    }

    public String getToLimit() {
        return toLimit;
    }

    public void setToLimit(String toLimit) {
        this.toLimit = toLimit;
    }



}

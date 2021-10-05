package com.commercial.tuds.earnandpay.Models;

public class Notification {

    private String notificationTitle;
    private String notificationMessage;

    public Notification() {
    }

    public Notification(String notificationTitle, String notificationMessage) {
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationTitle='" + notificationTitle + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                '}';
    }
}

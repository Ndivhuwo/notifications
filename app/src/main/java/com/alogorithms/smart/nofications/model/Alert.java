package com.alogorithms.smart.nofications.model;

import com.alogorithms.smart.nofications.model.enumaration.AlertType;

public class Alert {

    private String id;
    private AlertType alertType;
    private double latitude;
    private double longitude;
    private String address;
    private String message;
    private String userIdentification;
    private String imageLink;
    private String videoLink;

    public Alert() {
    }

    public Alert(AlertType alertType, double latitude, double longitude, String address, String message, String userIdentification) {
        this.alertType = alertType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.message = message;
        this.userIdentification = userIdentification;
    }

    public Alert(AlertType alertType, double latitude, double longitude, String address, String message, String userIdentification, String imageLink) {
        this.alertType = alertType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.message = message;
        this.userIdentification = userIdentification;
        this.imageLink = imageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserIdentification() {
        return userIdentification;
    }

    public void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}

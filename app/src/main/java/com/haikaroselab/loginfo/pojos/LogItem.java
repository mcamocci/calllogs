package com.haikaroselab.loginfo.pojos;

/**
 * Created by root on 1/5/17.
 */

public class LogItem {

    public LogItem(){

    }
    private String phoneNumber;
    private String timeCalled;
    private String timeEnded;
    private String timeAccepted;
    private String callDuration;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getTimeAccepted() {
        return timeAccepted;
    }

    public void setTimeAccepted(String timeAccepted) {
        this.timeAccepted = timeAccepted;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTimeCalled() {
        return timeCalled;
    }

    public void setTimeCalled(String timeCalled) {
        this.timeCalled = timeCalled;
    }

    public String getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(String timeEnded) {
        this.timeEnded = timeEnded;
    }

    public String getCallDuration() {
        return this.callDuration;
    }

    public void setCallDuration(String duration) {
        this.callDuration = duration;
    }
}

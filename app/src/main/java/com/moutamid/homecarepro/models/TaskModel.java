package com.moutamid.homecarepro.models;

public class TaskModel {
    String id, name, description;
    long date;  // timestamp
    String priority; // low, medium, high
    String frequency; //  weekly, quarter(3 months), monthly
    boolean status; // complete or not. (true/false)
    String startingDate;
    long startingDateTimeStamp;

    public TaskModel(String id, String name, String description, long date, String priority, String frequency, boolean status, String startingDate, long startingDateTimeStamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.frequency = frequency;
        this.status = status;
        this.startingDate = startingDate;
        this.startingDateTimeStamp = startingDateTimeStamp;
    }

    public long getStartingDateTimeStamp() {
        return startingDateTimeStamp;
    }

    public void setStartingDateTimeStamp(long startingDateTimeStamp) {
        this.startingDateTimeStamp = startingDateTimeStamp;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

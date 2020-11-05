package com.app.root.motorkart.modelclass;

public class BookingStatusModel {
    String id,created_on,message,status,fname,lname,registration_num,name;

    public BookingStatusModel(String id, String created_on, String message,
                              String status, String fname, String lname,
                              String registration_num, String name) {
       this.setId(id);
       this.setCreated_on(created_on);
       this.setMessage(message);
       this.setStatus(status);
       this.setFname(fname);
       this.setLname(lname);
       this.setRegistration_num(registration_num);
       this.setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRegistration_num() {
        return registration_num;
    }

    public void setRegistration_num(String registration_num) {
        this.registration_num = registration_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

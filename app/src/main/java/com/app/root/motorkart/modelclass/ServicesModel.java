package com.app.root.motorkart.modelclass;

public class ServicesModel {
    String id,cust_id,vid,storeId,message,status,isDeleted,created_on,registration_num,mainCategId;

    public ServicesModel(String id, String cust_id, String vid, String storeId, String message, String status,
                         String isDeleted, String created_on, String registration_num, String mainCategId) {
       this.setId(id);
       this.setCust_id(cust_id);
       this.setVid(vid);
       this.setStoreId(storeId);
       this.setMessage(message);
       this.setStatus(status);
       this.setIsDeleted(isDeleted);
       this.setCreated_on(created_on);
       this.setRegistration_num(registration_num);
       this.setMainCategId(mainCategId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getRegistration_num() {
        return registration_num;
    }

    public void setRegistration_num(String registration_num) {
        this.registration_num = registration_num;
    }

    public String getMainCategId() {
        return mainCategId;
    }

    public void setMainCategId(String mainCategId) {
        this.mainCategId = mainCategId;
    }
}

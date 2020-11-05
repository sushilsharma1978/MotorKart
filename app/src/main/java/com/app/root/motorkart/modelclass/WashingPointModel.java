package com.app.root.motorkart.modelclass;

public class WashingPointModel {
    String sid,cid,name,email,phone,store_num,instt_charge,discount,ratings,address,state_id,city_id,
            area_id,location, timings,isDeleted,status,created_on;

    public WashingPointModel(String sid, String cid, String name, String email, String phone,
                            String store_num, String instt_charge, String discount, String ratings,
                            String address, String state_id, String city_id, String area_id,
                            String location, String timings, String isDeleted, String status,String created_on ) {
        this.setSid(sid);
        this.setCid(cid);
        this.setEmail(email);
        this.setPhone(phone);
        this.setStore_num(store_num);
        this.setInstt_charge(instt_charge);
        this.setDiscount(discount);
        this.setRatings(ratings);
        this.setAddress(address);
        this.setState_id(state_id);
        this.setCity_id(city_id);
        this.setName(name);
        this.setArea_id(area_id);
        this.setLocation(location);
        this.setTimings(timings);
        this.setIsDeleted(isDeleted);
        this.setStatus(status);
        this.setStatus(status);
        this.setStatus(status);
this.setCreated_on(created_on);
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStore_num() {
        return store_num;
    }

    public void setStore_num(String store_num) {
        this.store_num = store_num;
    }

    public String getInstt_charge() {
        return instt_charge;
    }

    public void setInstt_charge(String instt_charge) {
        this.instt_charge = instt_charge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}

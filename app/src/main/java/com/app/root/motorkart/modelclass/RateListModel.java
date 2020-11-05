package com.app.root.motorkart.modelclass;

public class RateListModel {
    String option_id,store_id,name,price,status,isDeleted,created_on;

    public RateListModel(String option_id, String store_id, String name, String price,
                         String status, String isDeleted, String created_on) {
      this.setOption_id(option_id);
      this.setStore_id(store_id);
      this.setName(name);
      this.setPrice(price);
      this.setStatus(status);
      this.setIsDeleted(isDeleted);
      this.setCreated_on(created_on);
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}

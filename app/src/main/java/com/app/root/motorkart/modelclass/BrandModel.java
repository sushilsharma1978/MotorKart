package com.app.root.motorkart.modelclass;

public class BrandModel {
    String brand_name,image,brand_id;

    public BrandModel(String brand_name, String image,String brand_id) {
        this.setBrand_name(brand_name);
       this.setImage(image);
       this.setBrand_id(brand_id);
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }
}

package com.app.root.motorkart.modelclass;

public class VehicleModel {
    String id,cid,mainCategId,brandId,modelId,fuelType,registration_num,name,status,isDeleted,modified_on,
            created_on,model_name,brand_name;

    public VehicleModel(String id, String cid, String mainCategId, String brandId, String modelId,
                        String fuelType, String registration_num, String name, String status,
                        String isDeleted, String modified_on, String created_on,String model_name,String brand_name) {
       this.setId(id);
       this.setCid(cid);
       this.setMainCategId(mainCategId);
       this.setBrandId(brandId);
       this.setModelId(modelId);
       this.setFuelType(fuelType);
       this.setRegistration_num(registration_num);
       this.setName(name);
       this.setStatus(status);
       this.setIsDeleted(isDeleted);
       this.setModified_on(modified_on);
       this.setCreated_on(created_on);
       this.setBrand_name(brand_name);
       this.setModel_name(model_name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMainCategId() {
        return mainCategId;
    }

    public void setMainCategId(String mainCategId) {
        this.mainCategId = mainCategId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
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

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}

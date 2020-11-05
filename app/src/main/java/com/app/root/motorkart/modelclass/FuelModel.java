package com.app.root.motorkart.modelclass;

public class FuelModel {
    String type,id;

    public FuelModel(String type,String id) {
        this.setType(type);
        this.setId(id);

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

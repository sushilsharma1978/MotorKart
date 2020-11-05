package com.app.root.motorkart.modelclass;

public class ChooseVehiclemodel {
    String category_id,name,image;

    public ChooseVehiclemodel(String category_id, String name, String image) {
        this.setCategory_id(category_id);
        this.setName(name);
        this.setImage(image);
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

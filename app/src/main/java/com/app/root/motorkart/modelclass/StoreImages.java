package com.app.root.motorkart.modelclass;

public class StoreImages {
    String gid,store_id,image;

    public StoreImages(String gid, String store_id, String image) {
      this.setGid(gid);
      this.setStore_id(store_id);
      this.setImage(image);
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

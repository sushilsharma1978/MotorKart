package com.app.root.motorkart.modelclass;

public class StoreModel {
    String tid,name,avtar;

    public StoreModel(String tid, String name, String avtar) {
        this.setTid(tid);
        this.setName(name);
        this.setAvtar(avtar);
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvtar() {
        return avtar;
    }

    public void setAvtar(String avtar) {
        this.avtar = avtar;
    }
}

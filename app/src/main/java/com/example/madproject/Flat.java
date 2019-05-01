package com.example.madproject;

public class Flat {

    private String flatId;
    private  String houseId;
    private String keyValue;


    public Flat(){

    }

    public Flat(String flatId, String houdeid, String keyValue) {
        this.flatId = flatId;
        this.houseId = houdeid;
        this.keyValue = keyValue;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}

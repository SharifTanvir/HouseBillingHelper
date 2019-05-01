package com.example.madproject;

public class Tenant {

    private String tenantKey;
    private  String flatId;
    private String name;
    private String phone;
    private  String nid;
    private String status;

    public Tenant() {

    }

    public Tenant(String tenantKey, String flatId, String name, String phone, String nid, String status) {
        this.tenantKey = tenantKey;
        this.flatId = flatId;
        this.name = name;
        this.phone = phone;
        this.nid = nid;
        this.status = status;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

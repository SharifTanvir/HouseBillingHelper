package com.example.madproject;

import java.util.Date;

public class Bill {


    private String month;
    private float rent;
    private float meterReading;
    private float electricityBill;
    private float total;
    private String rentStatus;
    private String electricityStatus;
    private String billKey;
    private String flatId;
    private String date;

    public Bill(){

    }

    public Bill(String month, float rent, float meterReading, float electricityBill, float total, String rentStatus, String electricityStatus, String billKey, String flatId, String date) {
        this.month = month;
        this.rent = rent;
        this.meterReading = meterReading;
        this.electricityBill = electricityBill;
        this.total = total;
        this.rentStatus = rentStatus;
        this.electricityStatus = electricityStatus;
        this.billKey = billKey;
        this.flatId = flatId;
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public float getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(float meterReading) {
        this.meterReading = meterReading;
    }

    public float getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(float electricityBill) {
        this.electricityBill = electricityBill;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getElectricityStatus() {
        return electricityStatus;
    }

    public void setElectricityStatus(String electricityStatus) {
        this.electricityStatus = electricityStatus;
    }

    public String getBillKey() {
        return billKey;
    }

    public void setBillKey(String billKey) {
        this.billKey = billKey;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

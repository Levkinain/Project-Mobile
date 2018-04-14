package com.netcracker.myapplication.Entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class OrderEntityTO {

    public static final String ORDER_COMPLETE = "Заказ завершен";
    public static final String ORDER_CANCELED = "Заказ отменен";
    public static final String PICKED_CLIENT = "Водитель с клиентом";
    public static final String DRIVER_TO_CLIENT = "Водитель движется к клиенту";

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("typeId")
    @Expose
    private long typeId;

    @SerializedName("clientFirstName")
    @Expose
    private String clientFirstName;

    @SerializedName("clientLastName")
    @Expose
    private String clientLastName;

    @SerializedName("clientPhoneNumber")
    @Expose
    private String clientPhoneNumber;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("driverId")
    @Expose
    private long driverId;

    @SerializedName("orderCost")
    @Expose
    private String orderCost;

    @SerializedName("geoData")
    @Expose
    private String geoData;

    @SerializedName("destinationGeoData")
    @Expose
    private String destinationGeoData;

    @SerializedName("orderStartTime")
    @Expose
    private String orderStartTime;

    @SerializedName("orderEndTime")
    @Expose
    private String orderEndTime;

    @SerializedName("statusOrder")
    @Expose
    private String statusOrder;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getGeoData() {
        return geoData;
    }

    public void setGeoData(String geoData) {
        this.geoData = geoData;
    }

    public String getDestinationGeoData() {
        return destinationGeoData;
    }

    public void setDestinationGeoData(String destinationGeoData) {
        this.destinationGeoData = destinationGeoData;
    }

    public String getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public String getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(String orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public OrderEntityTO() {
    }

    public OrderEntityTO(long id, String name, long typeId, String clientFirstName, String clientLastName, String clientPhoneNumber, String address, long driverId, String orderCost, String geoData, String destinationGeoData, String orderStartTime, String orderEndTime, String statusOrder) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientPhoneNumber = clientPhoneNumber;
        this.address = address;
        this.driverId = driverId;
        this.orderCost = orderCost;
        this.geoData = geoData;
        this.destinationGeoData = destinationGeoData;
        this.orderStartTime = orderStartTime;
        this.orderEndTime = orderEndTime;
        this.statusOrder = statusOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntityTO that = (OrderEntityTO) o;
        return getId() == that.getId() &&
                getTypeId() == that.getTypeId() &&
                getDriverId() == that.getDriverId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getClientFirstName(), that.getClientFirstName()) &&
                Objects.equals(getClientLastName(), that.getClientLastName()) &&
                Objects.equals(getClientPhoneNumber(), that.getClientPhoneNumber()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getOrderCost(), that.getOrderCost()) &&
                Objects.equals(getGeoData(), that.getGeoData()) &&
                Objects.equals(getDestinationGeoData(), that.getDestinationGeoData()) &&
                Objects.equals(getOrderStartTime(), that.getOrderStartTime()) &&
                Objects.equals(getOrderEndTime(), that.getOrderEndTime()) &&
                Objects.equals(getStatusOrder(), that.getStatusOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTypeId(), getClientFirstName(), getClientLastName(), getClientPhoneNumber(), getAddress(), getDriverId(), getOrderCost(), getGeoData(), getDestinationGeoData(), getOrderStartTime(), getOrderEndTime(), getStatusOrder());
    }

    @Override
    public String toString() {
        return "OrderEntityTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", driverId=" + driverId +
                ", orderCost='" + orderCost + '\'' +
                ", geoData='" + geoData + '\'' +
                ", destinationGeoData='" + destinationGeoData + '\'' +
                ", orderStartTime='" + orderStartTime + '\'' +
                ", orderEndTime='" + orderEndTime + '\'' +
                ", statusOrder='" + statusOrder + '\'' +
                '}';
    }

}

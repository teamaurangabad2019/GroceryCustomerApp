package com.teammandroid.dairyapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderModel implements Parcelable {

    int orderid;
    int userid;
    int status;
    int deliveryboyid;

    @Override
    public String toString() {
        return "OrderModel{" +
                "orderid=" + orderid +
                ", userid=" + userid +
                ", status=" + status +
                ", deliveryboyid=" + deliveryboyid +
                ", deliverydate='" + deliverydate + '\'' +
                ", address='" + address + '\'' +
                ", paymentmode=" + paymentmode +
                ", totalprice=" + totalprice +
                ", savedprice=" + savedprice +
                ", isactive=" + isactive +
                ", created='" + created + '\'' +
                ", createdby=" + createdby +
                ", modified='" + modified + '\'' +
                ", modifiedby=" + modifiedby +
                ", RowCount=" + RowCount +
                '}';
    }

    String deliverydate;
    String address;
    int paymentmode;
    double totalprice;
    double savedprice;
    int isactive;
    String created;
    int createdby;
    String modified;
    int modifiedby;
    int RowCount;

    protected OrderModel(Parcel in) {
        orderid = in.readInt();
        userid = in.readInt();
        status = in.readInt();
        deliveryboyid = in.readInt();
        deliverydate = in.readString();
        address = in.readString();
        paymentmode = in.readInt();
        totalprice = in.readDouble();
        savedprice = in.readDouble();
        isactive = in.readInt();
        created = in.readString();
        createdby = in.readInt();
        modified = in.readString();
        modifiedby = in.readInt();
        RowCount = in.readInt();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public OrderModel(int orderid, int userid, int status, int deliveryboyid, String deliverydate, String address, int paymentmode, double totalprice, double savedprice, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {
        this.orderid = orderid;
        this.userid = userid;
        this.status = status;
        this.deliveryboyid = deliveryboyid;
        this.deliverydate = deliverydate;
        this.address = address;
        this.paymentmode = paymentmode;
        this.totalprice = totalprice;
        this.savedprice = savedprice;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(orderid);
        parcel.writeInt(userid);
        parcel.writeInt(status);
        parcel.writeInt(deliveryboyid);
        parcel.writeString(deliverydate);
        parcel.writeString(address);
        parcel.writeInt(paymentmode);
        parcel.writeDouble(totalprice);
        parcel.writeDouble(savedprice);
        parcel.writeInt(isactive);
        parcel.writeString(created);
        parcel.writeInt(createdby);
        parcel.writeString(modified);
        parcel.writeInt(modifiedby);
        parcel.writeInt(RowCount);
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeliveryboyid() {
        return deliveryboyid;
    }

    public void setDeliveryboyid(int deliveryboyid) {
        this.deliveryboyid = deliveryboyid;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(int paymentmode) {
        this.paymentmode = paymentmode;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public double getSavedprice() {
        return savedprice;
    }

    public void setSavedprice(double savedprice) {
        this.savedprice = savedprice;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(int modifiedby) {
        this.modifiedby = modifiedby;
    }

    public int getRowCount() {
        return RowCount;
    }

    public void setRowCount(int rowCount) {
        RowCount = rowCount;
    }

    public static Creator<OrderModel> getCREATOR() {
        return CREATOR;
    }
}
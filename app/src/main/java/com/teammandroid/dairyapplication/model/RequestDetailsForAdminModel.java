package com.teammandroid.dairyapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestDetailsForAdminModel implements Parcelable {

    /* String Productname;
     String fullname;*/
    int orderid;
    int userid;
    int status;
    int deliveryboyid;
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

    protected RequestDetailsForAdminModel(Parcel in) {
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

    public RequestDetailsForAdminModel(int orderid, int userid, int status, int deliveryboyid, String deliverydate, String address, int paymentmode, double totalprice, double savedprice, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderid);
        dest.writeInt(userid);
        dest.writeInt(status);
        dest.writeInt(deliveryboyid);
        dest.writeString(deliverydate);
        dest.writeString(address);
        dest.writeInt(paymentmode);
        dest.writeDouble(totalprice);
        dest.writeDouble(savedprice);
        dest.writeInt(isactive);
        dest.writeString(created);
        dest.writeInt(createdby);
        dest.writeString(modified);
        dest.writeInt(modifiedby);
        dest.writeInt(RowCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestDetailsForAdminModel> CREATOR = new Creator<RequestDetailsForAdminModel>() {
        @Override
        public RequestDetailsForAdminModel createFromParcel(Parcel in) {
            return new RequestDetailsForAdminModel(in);
        }

        @Override
        public RequestDetailsForAdminModel[] newArray(int size) {
            return new RequestDetailsForAdminModel[size];
        }
    };

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

    public static Creator<RequestDetailsForAdminModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "RequestDetailsForAdminModel{" +
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
}
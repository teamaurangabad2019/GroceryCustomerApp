package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryboyStatusModel implements Parcelable {

    String fullname;
    String mobile;
    String email;
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

    protected DeliveryboyStatusModel(Parcel in) {
        fullname = in.readString();
        mobile = in.readString();
        email = in.readString();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullname);
        dest.writeString(mobile);
        dest.writeString(email);
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

    public static final Creator<DeliveryboyStatusModel> CREATOR = new Creator<DeliveryboyStatusModel>() {
        @Override
        public DeliveryboyStatusModel createFromParcel(Parcel in) {
            return new DeliveryboyStatusModel(in);
        }

        @Override
        public DeliveryboyStatusModel[] newArray(int size) {
            return new DeliveryboyStatusModel[size];
        }
    };

    @Override
    public String toString() {
        return "DeliveryboyStatusModel{" +
                "fullname='" + fullname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", orderid=" + orderid +
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

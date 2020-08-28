package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderproductModel implements Parcelable {

     int orderproductid;
     int orderid;
     int productid;
     int isactive;
     String created;
     int createdby;
     String modified;
     int modifiedby;
     int RowCount;

    public OrderproductModel(int orderproductid, int orderid, int productid, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {
        this.orderproductid = orderproductid;
        this.orderid = orderid;
        this.productid = productid;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    protected OrderproductModel(Parcel in) {
        orderproductid = in.readInt();
        orderid = in.readInt();
        productid = in.readInt();
        isactive = in.readInt();
        created = in.readString();
        createdby = in.readInt();
        modified = in.readString();
        modifiedby = in.readInt();
        RowCount = in.readInt();
    }

    public static final Creator<OrderproductModel> CREATOR = new Creator<OrderproductModel>() {
        @Override
        public OrderproductModel createFromParcel(Parcel in) {
            return new OrderproductModel(in);
        }

        @Override
        public OrderproductModel[] newArray(int size) {
            return new OrderproductModel[size];
        }
    };

    @Override
    public String toString() {
        return "OrderproductModel{" +
                "orderproductid=" + orderproductid +
                ", orderid=" + orderid +
                ", productid=" + productid +
                ", isactive=" + isactive +
                ", created='" + created + '\'' +
                ", createdby=" + createdby +
                ", modified='" + modified + '\'' +
                ", modifiedby=" + modifiedby +
                ", RowCount=" + RowCount +
                '}';
    }

    public int getOrderproductid() {
        return orderproductid;
    }

    public void setOrderproductid(int orderproductid) {
        this.orderproductid = orderproductid;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderproductid);
        dest.writeInt(orderid);
        dest.writeInt(productid);
        dest.writeInt(isactive);
        dest.writeString(created);
        dest.writeInt(createdby);
        dest.writeString(modified);
        dest.writeInt(modifiedby);
        dest.writeInt(RowCount);
    }
}

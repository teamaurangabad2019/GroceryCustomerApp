package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuantitySqliteModel implements Parcelable {

    int productid;
    int usreid;
    int count;

    public QuantitySqliteModel() {
    }

    public QuantitySqliteModel(int productid, int usreid, int count) {
        this.productid = productid;
        this.usreid = usreid;
        this.count = count;
    }

    protected QuantitySqliteModel(Parcel in) {
        productid = in.readInt();
        usreid = in.readInt();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productid);
        dest.writeInt(usreid);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuantitySqliteModel> CREATOR = new Creator<QuantitySqliteModel>() {
        @Override
        public QuantitySqliteModel createFromParcel(Parcel in) {
            return new QuantitySqliteModel(in);
        }

        @Override
        public QuantitySqliteModel[] newArray(int size) {
            return new QuantitySqliteModel[size];
        }
    };

    @Override
    public String toString() {
        return "QuantitySqliteModel{" +
                "productid=" + productid +
                ", usreid=" + usreid +
                ", count=" + count +
                '}';
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getUsreid() {
        return usreid;
    }

    public void setUsreid(int usreid) {
        this.usreid = usreid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductImageModel implements Parcelable {
    int imageid;
    int productid;
    String imagename;
    int  isactive;
    String created;
    int createdby;
    String modified;
    int modifiedby;
    int RowCount;


    public ProductImageModel(int imageid, int productid, String imagename, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {

        this.imageid = imageid;
        this.productid = productid;
        this.imagename = imagename;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    protected ProductImageModel(Parcel in) {
        imageid = in.readInt();
        productid = in.readInt();
        imagename = in.readString();
        isactive = in.readInt();
        created = in.readString();
        createdby = in.readInt();
        modified = in.readString();
        modifiedby = in.readInt();
        RowCount = in.readInt();
    }

    public static final Creator<ProductImageModel> CREATOR = new Creator<ProductImageModel>() {
        @Override
        public ProductImageModel createFromParcel(Parcel in) {
            return new ProductImageModel(in);
        }

        @Override
        public ProductImageModel[] newArray(int size) {
            return new ProductImageModel[size];
        }
    };

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
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
    public String toString() {
        return "ProductImageModel{" +
                "imageid=" + imageid +
                ", productid=" + productid +
                ", imagename='" + imagename + '\'' +
                ", isactive=" + isactive +
                ", created='" + created + '\'' +
                ", createdby=" + createdby +
                ", modified='" + modified + '\'' +
                ", modifiedby=" + modifiedby +
                ", RowCount=" + RowCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageid);
        dest.writeInt(productid);
        dest.writeString(imagename);
        dest.writeInt(isactive);
        dest.writeString(created);
        dest.writeInt(createdby);
        dest.writeString(modified);
        dest.writeInt(modifiedby);
        dest.writeInt(RowCount);
    }
}

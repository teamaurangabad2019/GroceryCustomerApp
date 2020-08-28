package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemsSqlite implements Parcelable {

    int bookingid;
    int productid;
    String title;
    String details;
    double price;
    double ourprice;
    int offer;
    int isavailable;
    int subcategory;
    String imagename;
    int isactive;
    String created;
    int createdby;
    String modified;
    int modifiedby;
    int RowCount;

    public ItemsSqlite() {
    }

    public ItemsSqlite(int bookingid, int productid, String title, String details, double price, double ourprice, int offer, int isavailable, int subcategory, String imagename, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {
        this.bookingid = bookingid;
        this.productid = productid;
        this.title = title;
        this.details = details;
        this.price = price;
        this.ourprice = ourprice;
        this.offer = offer;
        this.isavailable = isavailable;
        this.subcategory = subcategory;
        this.imagename = imagename;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    protected ItemsSqlite(Parcel in) {
        bookingid = in.readInt();
        productid = in.readInt();
        title = in.readString();
        details = in.readString();
        price = in.readDouble();
        ourprice = in.readDouble();
        offer = in.readInt();
        isavailable = in.readInt();
        subcategory = in.readInt();
        imagename = in.readString();
        isactive = in.readInt();
        created = in.readString();
        createdby = in.readInt();
        modified = in.readString();
        modifiedby = in.readInt();
        RowCount = in.readInt();
    }

    public static final Creator<ItemsSqlite> CREATOR = new Creator<ItemsSqlite>() {
        @Override
        public ItemsSqlite createFromParcel(Parcel in) {
            return new ItemsSqlite(in);
        }

        @Override
        public ItemsSqlite[] newArray(int size) {
            return new ItemsSqlite[size];
        }
    };

    @Override
    public String toString() {
        return "ItemsSqlite{" +
                "bookingid=" + bookingid +
                ", productid=" + productid +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", ourprice=" + ourprice +
                ", offer=" + offer +
                ", isavailable=" + isavailable +
                ", subcategory=" + subcategory +
                ", imagename='" + imagename + '\'' +
                ", isactive=" + isactive +
                ", created='" + created + '\'' +
                ", createdby=" + createdby +
                ", modified='" + modified + '\'' +
                ", modifiedby=" + modifiedby +
                ", RowCount=" + RowCount +
                '}';
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOurprice() {
        return ourprice;
    }

    public void setOurprice(double ourprice) {
        this.ourprice = ourprice;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public int getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(int isavailable) {
        this.isavailable = isavailable;
    }

    public int getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int subcategory) {
        this.subcategory = subcategory;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookingid);
        dest.writeInt(productid);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeDouble(price);
        dest.writeDouble(ourprice);
        dest.writeInt(offer);
        dest.writeInt(isavailable);
        dest.writeInt(subcategory);
        dest.writeString(imagename);
        dest.writeInt(isactive);
        dest.writeString(created);
        dest.writeInt(createdby);
        dest.writeString(modified);
        dest.writeInt(modifiedby);
        dest.writeInt(RowCount);
    }
}

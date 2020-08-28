package com.teammandroid.dairyapplication.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubcategoryModel implements Parcelable {

      int subcategoryid;
      int categoryid;
      String title;
      String details;
      String imagename;
      int isactive;
      String created;
      int createdby;
      String modified;
      int modifiedby;
      int RowCount;

    public SubcategoryModel(int subcategoryid, int categoryid, String title, String details, String imagename, int isactive, String created, int createdby, String modified, int modifiedby, int rowCount) {
        this.subcategoryid = subcategoryid;
        this.categoryid = categoryid;
        this.title = title;
        this.details = details;
        this.imagename = imagename;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    protected SubcategoryModel(Parcel in) {
        subcategoryid = in.readInt();
        categoryid = in.readInt();
        title = in.readString();
        details = in.readString();
        imagename = in.readString();
        isactive = in.readInt();
        created = in.readString();
        createdby = in.readInt();
        modified = in.readString();
        modifiedby = in.readInt();
        RowCount = in.readInt();
    }

    public static final Creator<SubcategoryModel> CREATOR = new Creator<SubcategoryModel>() {
        @Override
        public SubcategoryModel createFromParcel(Parcel in) {
            return new SubcategoryModel(in);
        }

        @Override
        public SubcategoryModel[] newArray(int size) {
            return new SubcategoryModel[size];
        }
    };

    @Override
    public String toString() {
        return "SubcategoryModel{" +
                "subcategoryid=" + subcategoryid +
                ", categoryid=" + categoryid +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", imagename='" + imagename + '\'' +
                ", isactive=" + isactive +
                ", created='" + created + '\'' +
                ", createdby=" + createdby +
                ", modified='" + modified + '\'' +
                ", modifiedby=" + modifiedby +
                ", RowCount=" + RowCount +
                '}';
    }

    public int getSubcategoryid() {
        return subcategoryid;
    }

    public void setSubcategoryid(int subcategoryid) {
        this.subcategoryid = subcategoryid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(subcategoryid);
        parcel.writeInt(categoryid);
        parcel.writeString(title);
        parcel.writeString(details);
        parcel.writeString(imagename);
        parcel.writeInt(isactive);
        parcel.writeString(created);
        parcel.writeInt(createdby);
        parcel.writeString(modified);
        parcel.writeInt(modifiedby);
        parcel.writeInt(RowCount);
    }
}

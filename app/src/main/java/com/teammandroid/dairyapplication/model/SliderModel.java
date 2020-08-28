package com.teammandroid.dairyapplication.model;

public class SliderModel {

    int id;
    String images;
    String isactive;
    String created;
    String createdby;
    String modified;
    String modifiedby;
    String RowCount;

    public SliderModel(int id, String images, String isactive, String created, String createdby, String modified, String modifiedby, String rowCount) {
        this.id = id;
        this.images = images;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.modified = modified;
        this.modifiedby = modifiedby;
        RowCount = rowCount;
    }

    @Override
    public String toString() {
        return "SliderModel{" +
                "id=" + id +
                ", images='" + images + '\'' +
                ", isactive='" + isactive + '\'' +
                ", created='" + created + '\'' +
                ", createdby='" + createdby + '\'' +
                ", modified='" + modified + '\'' +
                ", modifiedby='" + modifiedby + '\'' +
                ", RowCount='" + RowCount + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getRowCount() {
        return RowCount;
    }

    public void setRowCount(String rowCount) {
        RowCount = rowCount;
    }
}

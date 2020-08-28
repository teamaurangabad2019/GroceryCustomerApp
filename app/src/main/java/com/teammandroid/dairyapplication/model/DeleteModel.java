package com.teammandroid.dairyapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeleteModel implements Parcelable {

    int TransactionId;

    protected DeleteModel(Parcel in) {
        TransactionId = in.readInt();
    }

    public DeleteModel(int transactionId) {
        TransactionId = transactionId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TransactionId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeleteModel> CREATOR = new Creator<DeleteModel>() {
        @Override
        public DeleteModel createFromParcel(Parcel in) {
            return new DeleteModel(in);
        }

        @Override
        public DeleteModel[] newArray(int size) {
            return new DeleteModel[size];
        }
    };

    @Override
    public String toString() {
        return "DeleteModel{" +
                "TransactionId=" + TransactionId +
                '}';
    }

    public int getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(int transactionId) {
        TransactionId = transactionId;
    }
}

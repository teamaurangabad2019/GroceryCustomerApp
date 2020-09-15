package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.AuthenticationActivity;
import com.teammandroid.dairyapplication.activities.BookingActivity;
import com.teammandroid.dairyapplication.admin.activities.CartActivity;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_4;
import static com.teammandroid.dairyapplication.utils.Constants.URL_PRODUCT_IMG;

public class CheckoutCartAdapter extends RecyclerView.Adapter<CheckoutCartAdapter.ViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity mContext;
    ArrayList<ProductModel> list;
    ArrayList<Double> totlAmt = new ArrayList<>();

    int offer =0 ;
    int subcategory = 0 ;
    int productId = 0;
    String title = " ";
    String details = " ";
    String image = " ";
    double price = 0.0;
    double ourprice = 0.0;

    DatabaseHelper dbHelper;
    PrefManager prefManager;

    public CheckoutCartAdapter(Activity context, String title, String details, double price, double ourprice, int offer, int subcategory, String image) {

        mContext = context;
        this.title = title;
        this.details = details;
        this.image = image;
        this.price = price;
        this.ourprice = ourprice;
    }

    public CheckoutCartAdapter(Activity context, ArrayList<ProductModel> models) {

        mContext = context;
        this.list = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_cart, parent, false);

        prefManager = new PrefManager(mContext);
        dbHelper = new DatabaseHelper(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ProductModel item = list.get(i);

        Log.d("adpaterCart" ,item.getDetails());

        viewHolder.tv_product_name.setText(item.getTitle());
        viewHolder.tv_price.setText("₹ "+String.valueOf(item.getOurprice()));
       // viewHolder.tv_qty.setText("0");
        //viewHolder.tv_qty.setText(String.valueOf(item.getRowCount()));
        //viewHolder.tv_amtsubtotal.setText(String.valueOf(item.getRowCount()));

        int sum = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " +
                dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " +
                item.getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            sum = cursor.getInt(0);//to get id, 0 is the column index

        }
        viewHolder.tv_qty.setText(String.valueOf(sum));
        double price =  sum * item.getOurprice();
        viewHolder.tv_amtsubtotal.setText(String.valueOf(price));


    }
  

    @Override
    public int getItemCount() {
        //return 0;
        return list.size();

        //Insert complete data in table
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView tv_product_name,tv_qty,tv_price,tv_amtsubtotal;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_amtsubtotal = itemView.findViewById(R.id.tv_amtsubtotal);

        }
    }

   


}


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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

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

    public CartAdapter(Activity context, String title, String details, double price, double ourprice, int offer, int subcategory, String image) {

        mContext = context;
        this.title = title;
        this.details = details;
        this.image = image;
        this.price = price;
        this.ourprice = ourprice;
    }

    public CartAdapter(Activity context, ArrayList<ProductModel> models) {

        mContext = context;
        this.list = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newcart, parent, false);

        prefManager = new PrefManager(mContext);
        dbHelper = new DatabaseHelper(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ProductModel item = list.get(i);

        Log.d("adpaterCart" ,item.getDetails());

        viewHolder.mTitle.setText(item.getTitle());
       /* viewHolder.mDesp.setText(item.getDetails());
        viewHolder.mOffer.setText("Rs " + item.getPrice());
       */
       // viewHolder.mOfferprice.setText(String.valueOf("("+item.getOffer() + "% off ) "));
        viewHolder.mPrice.setText(String.valueOf(item.getOurprice()));
        viewHolder.txt_quantity.setText(String.valueOf(item.getRowCount()));
        // viewHolder.txt_total_amount.setText(String.valueOf(item.getOurprice()));

        Picasso.with(mContext)
                .load(URL_PRODUCT_IMG + item.getImagename())
                .into(viewHolder.img);



        int sum = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " +
                dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " +
                item.getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            sum = cursor.getInt(0);//to get id, 0 is the column index

        }
        viewHolder.totalview.setText(String.valueOf(sum));
        double price =  sum * item.getOurprice();
        viewHolder.txt_total_amount.setText(String.valueOf(price));
        //  Log.d("CartAdapterd ",String.valueOf(id) + " "+price);


        viewHolder.iv_addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                addPmQuantity(item,viewHolder.totalview ,viewHolder.txt_total_amount);


            }
        });

        viewHolder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteQuantity(item,viewHolder.totalview, viewHolder.txt_total_amount);
            }
        });

        viewHolder.btn_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /***Checking User Already Loggedin or Not then Goto Booking Activity***/
                Bundle bundle = new Bundle();
                bundle.putParcelable("productModelCart",item);

                if (validateUser()) {
                    Utility.launchActivity(mContext, BookingActivity.class, false,bundle);
                } else {
                    Utility.launchActivity(mContext, AuthenticationActivity.class, false, bundle);
                }

            }
        });


        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogForDeleteRecord(item.getProductid(), prefManager.getUSER_ID() );

            }
        });

    }
    private void dialogForDeleteRecord(int productid, int userid){
        final Dialog resultbox = new Dialog(mContext);
        resultbox.setContentView(R.layout.delete_teacher_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_resume = (Button) resultbox.findViewById(R.id.btn_resume);

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                if (Utility.isNetworkAvailable(mContext)) {

                    SQLiteDatabase db=dbHelper.getWritableDatabase();

                    String sql1="DELETE FROM product WHERE  productid =  " +productid+ " AND userid = "
                            + userid;
                    db.execSQL(sql1);


                    String sql2="DELETE FROM quantity WHERE  productid =  " +productid+ " AND userid = "
                            + userid;
                    db.execSQL(sql1);
                    db.execSQL(sql2);

                    mContext.finish();
                    mContext.overridePendingTransition( 0, 0);
                    mContext.startActivity(mContext.getIntent());
                    mContext.overridePendingTransition( 0, 0);

                } else {
                    Utility.showErrorMessage(mContext, "No Internet Connection", Snackbar.LENGTH_SHORT);
                }
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }


    private boolean validateUser() {
        boolean result = false;
        try {
            SessionHelper sessionManager=new SessionHelper(mContext);
            //UserResponse response = PrefHandler.getUserFromSharedPref(SplashActivity.this);
            UserModel response = sessionManager.getUserDetails();
            Log.e(TAG, "validateUser: "+response.toString());
            if (response.getUserid() > 0) {
                result = true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "validateUser: ", ex);
        }
        return result;
    }


    @Override
    public int getItemCount() {
        //return 0;
        return list.size();

        //Insert complete data in table
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_cart,iv_remove,iv_addImg, iv_delete, img;
        RelativeLayout rl_delete;
        int count = 0;
        TextView mTitle, mDesp, mOfferprice, mOffer, mPrice, txt_quantity, txt_total_amount, btn_booknow;
        TextView totalview;

        public ViewHolder(View itemView) {
            super(itemView);
            mOffer = itemView.findViewById(R.id.offer);
            mOfferprice = itemView.findViewById(R.id.offerprice);
            mPrice = itemView.findViewById(R.id.tv_price);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDesp = itemView.findViewById(R.id.desc);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            btn_booknow = itemView.findViewById(R.id.btn_booknow);
            iv_cart = itemView.findViewById(R.id.iv_cart);
            img = itemView.findViewById(R.id.img);

            iv_remove = itemView.findViewById(R.id.iv_remove);
            totalview = itemView.findViewById(R.id.totalview);
            iv_addImg = itemView.findViewById(R.id.iv_addImg);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            mOffer.setPaintFlags(mOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            grandTotal();
            Toast.makeText(mContext, "mm"+grandTotal(), Toast.LENGTH_SHORT).show();
        }
    }


    private void addPmQuantity(ProductModel item,  TextView totalview,TextView txt_total_amount)
    {
        Log.d("CartAdapter ",String.valueOf(item.getProductid()));
        // if (!dbHelper.alreadyExistProductEntry(item.getProductid()))
        //{
        boolean isInserted = dbHelper.insertPmQuantity(
                item.getProductid(),//prefManager.getUSER_ID()
                prefManager.getUSER_ID(),
                1,
                item.getOurprice()
        );

        if (isInserted == true) {

            int id = 0;
            int productId = 0;
            int userid = 0;

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " +
                    dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " +
                    item.getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = "
                    + prefManager.getUSER_ID(), null);

            if (cursor.moveToNext()) {

                id = cursor.getInt(0);//to get id, 0 is the column index

            }
            totalview.setText(String.valueOf(id));
            double price =  id * item.getOurprice();
            txt_total_amount.setText(String.valueOf(price));
            Log.d("CartAdapterd ",String.valueOf(id) + " "+price);
            Toast.makeText(mContext, "Added Quantity " +id, Toast.LENGTH_SHORT).show();

            grandTotal();
            Toast.makeText(mContext, "added"+grandTotal(), Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private double grandTotal() {
        double totalOurPrice = 0;
        double totalPrice = 0;
        double totalSavedPrice = 0;
        for (int i = 0; i < list.size(); i++) {

            int sum = 0;
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " +
                    dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " +
                    list.get(i).getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = "
                    + prefManager.getUSER_ID(), null);

            if (cursor.moveToNext()) {

                sum = cursor.getInt(0);//to get id, 0 is the column index

            }
           // double price=list.get(i).getOurprice()*sum;
            totalOurPrice += list.get(i).getOurprice()* sum;
            totalPrice += list.get(i).getPrice()* sum;
            totalSavedPrice =totalPrice-totalOurPrice;
            prefManager.setour_price(String.valueOf(totalOurPrice));
            prefManager.setsaved_price(String.valueOf(totalSavedPrice));
        }
        return totalOurPrice;
    }


    private void deleteQuantity(ProductModel item, TextView tv, TextView txt_total_amount)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
/*
        DELETE FROM plusminusquantity WHERE pmquantityid = (SELECT MAX(pmquantityid )
        FROM plusminusquantity) and pmproductid = 20 AND pmuserid = 0;
*/
        String sql="DELETE FROM plusminusquantity WHERE pmquantityid = (SELECT MAX(pmquantityid )" +
                "FROM plusminusquantity where pmproductid = "
                +item.getProductid()+ " AND pmuserid = "+ String.valueOf(prefManager.getUSER_ID())+")";

        db.execSQL(sql);

        int id = 0;

        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        //Chnage userId static to dynamic
        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db2.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY
                + " WHERE " + PMQUANTITY_2 + " = " + item.getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            id = cursor.getInt(0);//to get id, 0 is the column index

        }
        tv.setText(String.valueOf(id));
        double price =  id * item.getOurprice();
        txt_total_amount.setText(String.valueOf(price));
        Log.d("CartAdapterd ",String.valueOf(id) + " "+price);

        grandTotal();
        Toast.makeText(mContext, "deleted"+grandTotal(), Toast.LENGTH_SHORT).show();

        // Toast.makeText(mContext, "Deleted " , Toast.LENGTH_SHORT).show();


    }



}


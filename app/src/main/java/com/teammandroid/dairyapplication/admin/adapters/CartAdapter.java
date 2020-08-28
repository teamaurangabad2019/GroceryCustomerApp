package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.AuthenticationActivity;
import com.teammandroid.dairyapplication.activities.BookingActivity;
import com.teammandroid.dairyapplication.admin.activities.CartActivity;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.SessionManager;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_3;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_4;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_1;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_PLUS_MINUS_QUANTITY;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_QUANTITY;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity mContext;
    ArrayList<ProductModel> list;

    int offer =0 ;
    int subcategory = 0 ;
    int productId = 0;
    String title = " ";
    String details = " ";
    String image = " ";
    double price = 0.0;
    double ourprice = 0.0;

    DatabaseHelper dbHelper;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        dbHelper = new DatabaseHelper(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ProductModel item = list.get(i);

        Log.d("adpaterCart" ,item.getDetails());

        viewHolder.mTitle.setText(item.getTitle());
        viewHolder.mDesp.setText(item.getDetails());
        viewHolder.mOffer.setText("Rs " + item.getPrice());
        viewHolder.mOfferprice.setText(String.valueOf("("+item.getOffer() + "% off ) "));
        viewHolder.mPrice.setText(String.valueOf(item.getOurprice()));
        viewHolder.txt_quantity.setText(String.valueOf(item.getRowCount()));
       // viewHolder.txt_total_amount.setText(String.valueOf(item.getOurprice()));

        viewHolder.iv_addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPmQuantity(item,viewHolder.totalview ,viewHolder.txt_total_amount);
            }
        });

        viewHolder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteQuantity(item,viewHolder.totalview);
            }
        });

        //viewHolder.txt_quantity.setText(String.valueOf(item.getNumber()));
        //viewHolder.txt_total_amount.setText(String.valueOf(item.getTotalprice()));

       /* viewHolder.rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(mContext, "Clicked Positive", Toast.LENGTH_SHORT).show();
                                BookingSqliteOperations bookingSqliteOperations = new BookingSqliteOperations(mContext);
                                bookingSqliteOperations.deleteBookings(item.getBookingid());

                                list.remove(i);
                                notifyDataSetChanged();
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, list.size());
                                // Show the removed item label`enter code here`
                                Toast.makeText(mContext, "Removed : " + item.getItemname(), Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });*/

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
               /* AppUserSqliteOperations appUserSqliteOperations = new AppUserSqliteOperations(mContext);
                AppUser appUser=appUserSqliteOperations.GetAppUser();

                if(appUser.getIslogin()==0){
                    Utility.launchActivity(mContext, OTPLoginActivity.class, false, bundle);
                }
                else{

                    Utility.launchActivity(mContext, BookingActivity.class, false, bundle);
                }*/
            }
        });
    }

    private boolean validateUser() {
        boolean result = false;
        try {
            SessionManager sessionManager=new SessionManager(mContext);
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

        ImageView iv_cart,iv_remove,iv_addImg;
        RelativeLayout rl_delete;
        int count = 0;
        TextView mTitle, mDesp, mOfferprice, mOffer, mPrice, txt_quantity, txt_total_amount, btn_booknow;
        TextView totalview;

        public ViewHolder(View itemView) {
            super(itemView);
            mOffer = itemView.findViewById(R.id.offer);
            mOfferprice = itemView.findViewById(R.id.offerprice);
            mPrice = itemView.findViewById(R.id.price);
            mTitle = itemView.findViewById(R.id.title);
            mDesp = itemView.findViewById(R.id.desc);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            btn_booknow = itemView.findViewById(R.id.btn_booknow);
            iv_cart = itemView.findViewById(R.id.iv_cart);
            rl_delete = itemView.findViewById(R.id.rl_delete);
            iv_remove = itemView.findViewById(R.id.iv_remove);
            totalview = itemView.findViewById(R.id.totalview);
            iv_addImg = itemView.findViewById(R.id.iv_addImg);

            mOffer.setPaintFlags(mOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


    private void addPmQuantity(ProductModel item,  TextView totalview,TextView txt_total_amount)
    {
            Log.d("CartAdapter ",String.valueOf(item.getProductid()));
       // if (!dbHelper.alreadyExistProductEntry(item.getProductid()))
        //{
            boolean isInserted = dbHelper.insertPmQuantity(
                    item.getProductid(),//prefManager.getUSER_ID()
                    0,
                    1,
                    item.getOurprice()
            );

            if (isInserted == true) {

                int id = 0;
                int productId = 0;
                int userid = 0;

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //Chnage userId static to dynamic
                //SELECT sum(count) from quantity where productid = 21;
                Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " + item.getProductid() +" AND " +dbHelper.PMQUANTITY_3 + " = " + " 0 ", null);

                if (cursor.moveToNext()) {

                    id = cursor.getInt(0);//to get id, 0 is the column index

                }
                totalview.setText(String.valueOf(id));
                double price =  id * item.getOurprice();
                txt_total_amount.setText(String.valueOf(price));
                Log.d("CartAdapterd ",String.valueOf(id) + " "+price);
                Toast.makeText(mContext, "Added Quantity " +id, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

       /* } else
        {
            Toast.makeText(mContext,"Go to Cart",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void deleteQuantity(ProductModel item, TextView tv)
    {
       // DELETE FROM plusminusquantity where pmproductid IN
       // (SELECT pmproductid FROM plusminusquantity where
       // pmproductid = 22 and pmuserid = 2 ORDER BY
       // pmproductid LIMIT 1 );

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        String sql=" DELETE FROM "+ TABLE_PLUS_MINUS_QUANTITY +" WHERE " + PMQUANTITY_2 + " IN  ( SELECT " + PMQUANTITY_2 + " FROM "
                + TABLE_PLUS_MINUS_QUANTITY +" WHERE " + PMQUANTITY_2 +" = "+ item.getProductid() +" AND "+ PMQUANTITY_3
                +" = "+ 0 +" ORDER BY " + PMQUANTITY_2 + " LIMIT 1 );";

        Cursor cursor=db.rawQuery(sql,null);

        int index=0 ;
        int index2=0 ;
        int id =0;

        if(cursor.moveToLast()){

            index = cursor.getColumnIndex("quantityid"); //quantityid

            id = cursor.getInt(index);//to get id, 0 is the column index
            showText(tv);
        }

        mContext.finish();
        mContext.overridePendingTransition( 0, 0);
        mContext.startActivity(mContext.getIntent());
        mContext.overridePendingTransition( 0, 0);

        Toast.makeText(mContext, "Deleted " , Toast.LENGTH_SHORT).show();


    }


    private void showText(TextView totalview) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM " + dbHelper.TABLE_QUANTITY, null);

        int id = 0;
        if(cursor.moveToNext()){

            id = cursor.getInt(0);//to get id, 0 is the column index
            //productId=cursor.getInt(1);
            //userid=cursor.getInt(1);
            //prefManager.setCOUNT_ID(id);

        }
        totalview.setText(String.valueOf(id));


    }

}


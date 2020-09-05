package com.teammandroid.dairyapplication.admin.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.Network.OrderProductServices;
import com.teammandroid.dairyapplication.Network.OrderServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.admin.adapters.CartAdapter;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_4;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CartActivity.class.getSimpleName();
    View viewMenuIconBack;
    View viewHome;
    Button btn_continue;
    ProgressDialog progressDialog;

    ArrayList<ProductModel> productModelslist;

    AppBarLayout appBarLayout;
    DatabaseHelper dbHelper;
    ArrayList product_array_list;
    Activity activity;
    int tquantity;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        progressDialog = new ProgressDialog(CartActivity.this);
        prefManager = new PrefManager(CartActivity.this);
        appBarLayout = findViewById(R.id.appBarLayout);
        dbHelper = new DatabaseHelper(CartActivity.this);
        activity=CartActivity.this;
        //Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, null);
        // drawable = DrawableCompat.wrap(drawable);
        //DrawableCompat.setTint(drawable, Color.WHITE);
        //toolbar.setOverflowIcon(drawable);

        bindView();
        btnListeneres();

        productModelslist = showProduct();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart);

        /*recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));*/
        recyclerView.setLayoutManager(layoutManager);
        CartAdapter adapter = new CartAdapter(this,productModelslist);
        recyclerView.setAdapter(adapter);

        product_array_list = dbHelper.getAllCotacts(String.valueOf(prefManager.getUSER_ID()));

        for(int i=0; i<product_array_list.size(); i++)

        {
            Log.e( "pchk: ", product_array_list.get(i).toString());
        }

        //initRecyclerViewMenus();

    }

    void bindView() {
        viewMenuIconBack = findViewById(R.id.viewMenuIconBack);
        viewHome = findViewById(R.id.viewHome);
        btn_continue = findViewById(R.id.btn_continue);
    }

    void btnListeneres() {
        viewMenuIconBack.setOnClickListener(this);
        viewHome.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
    }

    private ArrayList<ProductModel> showProduct() {

        int id = 0;
        int offer = 0;
        int subcategory = 0;
        int productId = 0;
        int isavailable = 0;
        int isactive = 0;
        String title = " ";
        String details = " ";
        String image = " ";
        String created = " ";
        String modified = " ";
        int createdby = 0;
        int modifiedby = 0;
        int RowCount = 0;
        double price = 0.0;
        double ourprice = 0.0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<ProductModel> list = new ArrayList<>();
        productModelslist = list;

        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_PRODUCT, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(dbHelper.CUST_1);
            int index2 = cursor.getColumnIndex(dbHelper.CUST_2);
            int index3 = cursor.getColumnIndex(dbHelper.CUST_3);
            int index4 = cursor.getColumnIndex(dbHelper.CUST_4);
            int index5 = cursor.getColumnIndex(dbHelper.CUST_5);
            int index6 = cursor.getColumnIndex(dbHelper.CUST_6);
            int index7 = cursor.getColumnIndex(dbHelper.CUST_7);
            int index8 = cursor.getColumnIndex(dbHelper.CUST_8);
            int index9 = cursor.getColumnIndex(dbHelper.CUST_9);
            int index10 = cursor.getColumnIndex(dbHelper.CUST_10);
            int index11 = cursor.getColumnIndex(dbHelper.CUST_11);
            int index12 = cursor.getColumnIndex(dbHelper.CUST_12);
            int index13 = cursor.getColumnIndex(dbHelper.CUST_13);
            int index14 = cursor.getColumnIndex(dbHelper.CUST_14);
            int index15 = cursor.getColumnIndex(dbHelper.CUST_15);
            int index16 = cursor.getColumnIndex(dbHelper.CUST_16);

            int productid = cursor.getInt(index2);
            title = cursor.getString(index3);
            details = cursor.getString(index4);
            price = cursor.getDouble(index5);
            ourprice = cursor.getDouble(index6);
            offer = cursor.getInt(index7);
            isavailable = cursor.getInt(index8);
            subcategory = cursor.getInt(index9);
            image = cursor.getString(index10);
            isactive = cursor.getInt(index11);
            created = cursor.getString(index12);
            createdby = cursor.getInt(index13);
            modified = cursor.getString(index14);
            modifiedby = cursor.getInt(index15);
            RowCount = cursor.getInt(index16);


            //DataBean bean = new DataBean(id,userId,serviceName,cityName,workingHours,monthlyCharges);
            //list.add(bean);

            ProductModel productModel = new ProductModel(productid, title,
                    details, price, ourprice, offer, isavailable, subcategory, image, isactive, created, createdby, modified
                    , modifiedby, RowCount);

            list.add(productModel);

        }
        return list;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewMenuIconBack:
                // Utility.launchActivity(CartActivity.this,HomeActivity.class,true);
                onBackPressed();
                break;

            case R.id.viewHome:
                Utility.launchActivity(CartActivity.this, HomepageActivity.class,true);
                break;

            case R.id.btn_continue:


                placeOrder(0,prefManager.getUSER_ID(),0,0,Utility.getCurrentDate(),
                        "Address",0,10,5);

                break;


        }
    }

    private boolean validateUser() {
        boolean result = false;
        try {
            SessionHelper sessionManager=new SessionHelper(CartActivity.this);
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


    private void placeOrder(int Orderid, int Userid,int Status,int Deliveryboyid,String Deliverydate,
                            String Address,int Paymentmode,double Totalprice,double Savedprice) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OrderServices.getInstance(getApplicationContext()).
                createOrder(Orderid,Userid,Status,Deliveryboyid,Deliverydate,
                        Address,Paymentmode,Totalprice,Savedprice, new ApiStatusCallBack<Response>() {
                            @Override
                            public void onSuccess(Response response) {
                                progressDialog.dismiss();
                                Log.e(TAG, "response " + response.getMessage());


                                Log.e( "chk: ", product_array_list.toString() );
                                for(int i=0; i<product_array_list.size(); i++)
                                {

                                    Log.e( "productlist: ",  product_array_list.get(i).toString());
                                    getQuantity(Integer.parseInt(product_array_list.get(i).toString()), response.getResult());
                                  /*  insertOrderProduct(0,response.getResult(),
                                            Integer.parseInt(product_array_list.get(i).toString()),tquantity,1,1);
*/
                                }
                                // imp
                                Utility.launchActivity(CartActivity.this,OrderHistoryActivity.class,true);
                                //Utility.launchActivity(CartActivity.this, EnquiryListActivity.class,false);
                                //  Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e(TAG, "ANError " + anError.getMessage());
                                Utility.showErrorMessage(CartActivity.this, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                Utility.showErrorMessage(CartActivity.this, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                        });

    }
    void insertOrderProduct(int Orderproductid,int Orderid, int Productid,int Quantity, double Totalamount,int LogedinUserId)
    {
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OrderProductServices.getInstance(activity).insertOrderProduct(
                Orderproductid ,Orderid,Productid,Quantity,Totalamount, LogedinUserId,
                new ApiStatusCallBack<Response>() {
                    @Override
                    public void onSuccess(Response userModels) {
                        Log.e("ChkOrderdetails:",userModels.getMessage());
                        Log.e("ChkOrderdetails:",userModels.toString());

                        if (userModels.getHasError()==1) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, userModels.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Utility.showErrorMessage(activity, userModels.getMessage());

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }
                });
    }

    void getQuantity(int pid, int oid)
    {
        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        Cursor cursor = db2.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY
                + " WHERE " + PMQUANTITY_2 + " = " + pid +" AND " +dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            tquantity = cursor.getInt(0);//to get id, 0 is the column index
            Log.e( "getQuantity: ", String.valueOf(tquantity));

        }
        //tv.setText(String.valueOf(id));
        // double price =  item * item.getOurprice();

        insertOrderProduct(0,oid,
                pid,tquantity,0,1);

        //  Log.e( "getQuantity: ", String.valueOf(tquantity));
    }
}
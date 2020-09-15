package com.teammandroid.dairyapplication.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.adapters.CartAdapter;
import com.teammandroid.dairyapplication.admin.adapters.WishlistAdapter;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ProductModel> productModelslist;
    DatabaseHelper dbHelper;
    View viewMenuIconBack;
    RelativeLayout childlayout,parentlayout;
    TextView txt_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        initView();

        productModelslist = showProduct();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

        Log.e( "onCreate: ", String.valueOf(productModelslist.size()));

        if(productModelslist.size()==0)
        {
            Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

        }
        else
        {
            WishlistAdapter adapter = new WishlistAdapter(this,productModelslist);
            recyclerView.setAdapter(adapter);
        }

    }

    private void initView() {
        dbHelper = new DatabaseHelper(WishlistActivity.this);
        viewMenuIconBack = findViewById(R.id.viewMenuIconBack);
        viewMenuIconBack.setOnClickListener(this);

        childlayout=findViewById(R.id.childlayout);
        parentlayout=findViewById(R.id.parentlayout);
        txt_error=findViewById(R.id.txt_error);
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_WISHLIST, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(dbHelper.WISH_1);
            int index2 = cursor.getColumnIndex(dbHelper.WISH_2);
            int index3 = cursor.getColumnIndex(dbHelper. WISH_3);
            int index4 = cursor.getColumnIndex(dbHelper. WISH_4);
            int index5 = cursor.getColumnIndex(dbHelper. WISH_5);
            int index6 = cursor.getColumnIndex(dbHelper. WISH_6);
            int index7 = cursor.getColumnIndex(dbHelper. WISH_7);
            int index8 = cursor.getColumnIndex(dbHelper. WISH_8);
            int index9 = cursor.getColumnIndex(dbHelper. WISH_9);
            int index10 = cursor.getColumnIndex(dbHelper.WISH_10);
            int index11 = cursor.getColumnIndex(dbHelper.WISH_11);
            int index12 = cursor.getColumnIndex(dbHelper.WISH_12);
            int index13 = cursor.getColumnIndex(dbHelper.WISH_13);
            int index14 = cursor.getColumnIndex(dbHelper.WISH_14);
            int index15 = cursor.getColumnIndex(dbHelper.WISH_15);
            int index16 = cursor.getColumnIndex(dbHelper.WISH_16);

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
                    , modifiedby, RowCount,0,0);

            Log.e( "showProduct: ", String.valueOf(productModel.getProductid()));
            list.add(productModel);

        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.viewMenuIconBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
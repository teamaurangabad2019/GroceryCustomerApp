package com.teammandroid.dairyapplication.activities;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.snackbar.Snackbar;
import com.steelkiwi.library.view.BadgeHolderLayout;
import com.teammandroid.dairyapplication.Network.ProductServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.adapters.ProductImagesAdapter;
import com.teammandroid.dairyapplication.admin.activities.CartActivity;
import com.teammandroid.dairyapplication.admin.model.ProductImageModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_4;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "ProductDetailsActivity" ;
    private ImageView iv_back;
    private ImageView iv_cart;
    private BadgeHolderLayout badgeLayout;
    private RecyclerView rv_images;
    /**
     * Name
     */
    private TextView title;
    /**
     * Name
     */
    private TextView weight;
    /**
     * ₹ Price
     */
    private TextView ourprice;
    /**
     * Name
     */
    private TextView offerprice;
    private ImageView iv_remove;
    /**
     * 0
     */
    private TextView totalview;
    private ImageView iv_addImg;
    private ImageView iv_wishlist;
    private LinearLayout ll_add;
    private RelativeLayout rl_price;
    private View view;
    /**
     * About the Product
     */
    private TextView abt;
    private TextView tv_offer;
    /**
     * o ok ok ok ok
     */
    private TextView tv_details;
    /**
     * Add to Cart
     */
    private Button btn_addCart;
    /**
     * Continue
     */
    private Button btn_continue;

    ProductModel productModel;
    ProgressDialog progressDialog;
    ProductImagesAdapter productImagesAdapter;
    private SliderLayout mDemoSlider;
    DatabaseHelper dbHelper;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();

        productModel = getIntent().getParcelableExtra("ProductModel");
        Log.d(TAG, " "+productModel.getProductid());

        getImages(productModel.getProductid());
        getCount();

        if (dbHelper.alreadyExistProductEntry(productModel.getProductid(),prefManager.getUSER_ID()))
        {
            iv_wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
            // holder.btn_allreadyadded.setText("Check cart");
        }else
        {
            iv_wishlist.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }

        title.setText(productModel.getTitle());
        tv_details.setText(productModel.getDetails());
        ourprice.setText(" ₹ "+String.valueOf(productModel.getOurprice()));
        tv_offer.setText(String.valueOf(productModel.getOffer()) +" % ");

        offerprice.setText(" ₹ "+String.valueOf(productModel.getPrice()));
        offerprice.setPaintFlags(offerprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (productModel.getIsavailable()==0)
        {
            weight.setText("Out of Stock");
            //holder.tv_pending.setTextColor(Color.parseColor("#00933C"));
            weight.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this, R.color.logoRed));
        }
        else
        {
            weight.setText("In Stock");
            weight.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this, R.color.colorPrimary));
        }


    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        badgeLayout = (BadgeHolderLayout) findViewById(R.id.badgeLayout);
        badgeLayout.setOnClickListener(this);
        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        title = (TextView) findViewById(R.id.title);
        weight = (TextView) findViewById(R.id.weight);
        ourprice = (TextView) findViewById(R.id.ourprice);
        offerprice = (TextView) findViewById(R.id.offerprice);
        iv_remove = (ImageView) findViewById(R.id.iv_remove);
        iv_remove.setOnClickListener(this);
        totalview = (TextView) findViewById(R.id.totalview);
        tv_offer = (TextView) findViewById(R.id.tv_offer);
        iv_addImg = (ImageView) findViewById(R.id.iv_addImg);
        iv_addImg.setOnClickListener(this);
        iv_wishlist = (ImageView) findViewById(R.id.iv_wishlist);
        iv_wishlist.setOnClickListener(this);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        view = (View) findViewById(R.id.view);
        abt = (TextView) findViewById(R.id.abt);
        tv_details = (TextView) findViewById(R.id.tv_details);
        btn_addCart = (Button) findViewById(R.id.btn_addCart);
        btn_addCart.setOnClickListener(this);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        dbHelper = new DatabaseHelper(ProductDetailsActivity.this);
        prefManager = new PrefManager(ProductDetailsActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_cart:
                break;
            case R.id.iv_wishlist:
                addWishlist(productModel.getProductid(), productModel.getTitle(),productModel.getDetails(),productModel.getPrice(),productModel.getOurprice(),
                        productModel.getOffer(),productModel.getIsavailable(),productModel.getSubcategory(),productModel.getImagename(),productModel.getIsactive(),
                        productModel.getCreated(),productModel.getCreatedby(),productModel.getModified(),
                        productModel.getModifiedby(),productModel.getRowCount(), prefManager.getUSER_ID());
                break;
            case R.id.badgeLayout:
                Utility.launchActivity(ProductDetailsActivity.this, CartActivity.class,false);
                //Utility.launchActivity(ProductDetailsActivity.this, CartActivity.class,false);
                break;
            case R.id.iv_remove:
                deleteQuantity(productModel.getProductid());
                break;
            case R.id.iv_addImg:
                Log.d(TAG,"iv_addImg "+productModel.getProductid()+" "+productModel.getOurprice());
                addPmQuantity(productModel.getProductid(),productModel.getOurprice());
                break;

            case R.id.btn_addCart:

                Log.d(TAG,"iv_addImg "+productModel.getProductid()+" "+productModel.getOurprice());

                if (validateUser()) {

                    addQuantity(productModel);

                } else {
                    Utility.launchActivity(ProductDetailsActivity.this, AuthUserActivity.class, false);//,bundle);

                }
                break;

            case R.id.btn_continue:
                Toast.makeText(ProductDetailsActivity.this,"Checkout",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void addPmQuantity(int productid,double ourPrice)
    {

        Log.d(TAG ,String.valueOf(productid));

        boolean isInserted = dbHelper.insertPmQuantity(
                productid,
                prefManager.getUSER_ID(),// prefManager.getUSER_ID(),
                1,
                ourPrice );

        if (isInserted == true) {

            int id = 0;
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " +
                    dbHelper.TABLE_PLUS_MINUS_QUANTITY + " WHERE " + PMQUANTITY_2 + " = " +
                    productid +" AND " +dbHelper.PMQUANTITY_3 + " = "
                    + prefManager.getUSER_ID(), null);

            if (cursor.moveToNext()) {

                id = cursor.getInt(0);//to get id, 0 is the column index

            }
            totalview.setText(String.valueOf(id));
            double price =  id * ourPrice;
            Log.d(TAG, " "+id);
            //txt_total_amount.setText(String.valueOf(price));
            Toast.makeText(ProductDetailsActivity.this, "Added Quantity " +id, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(ProductDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteQuantity(int productid)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
/*
        DELETE FROM plusminusquantity WHERE pmquantityid = (SELECT MAX(pmquantityid )
        FROM plusminusquantity) and pmproductid = 20 AND pmuserid = 0;
*/
        String sql="DELETE FROM plusminusquantity WHERE pmquantityid = (SELECT MAX(pmquantityid )" +
                "FROM plusminusquantity where pmproductid = "
                +productid+ " AND pmuserid = "+ String.valueOf(prefManager.getUSER_ID())+")";

        db.execSQL(sql);

        int id = 0;

        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        //Chnage userId static to dynamic
        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db2.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY
                + " WHERE " + PMQUANTITY_2 + " = " + productid +" AND " +dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            id = cursor.getInt(0);//to get id, 0 is the column index

        }
        totalview.setText(String.valueOf(id));
        // double price =  id * ourprice;
        //txt_total_amount.setText(String.valueOf(price));
        // Log.d("CartAdapterd ",String.valueOf(id) + " "+price);


    }

    private boolean validateUser() {
        boolean result = false;
        try {
            SessionHelper sessionHelper=new SessionHelper(ProductDetailsActivity.this);
            //UserResponse response = PrefHandler.getUserFromSharedPref(SplashActivity.this);
            UserModel response = sessionHelper.getUserDetails();
            //Log.e(TAG, "validateUser: "+response.toString());
            if (response.getUserid() > 0) {
                result = true;
            }
        } catch (Exception ex) {
            // Log.e(TAG, "validateUser: ", ex);
        }
        return result;
    }

    private void addQuantity(ProductModel item)
    {

        if (!dbHelper.alreadyExistProductEntry(item.getProductid(), prefManager.getUSER_ID()))
        {
            boolean isInserted = dbHelper.insertQuantity(
                    item.getProductid(),//prefManager.getUSER_ID()
                    prefManager.getUSER_ID(),
                    1
            );

            if (isInserted == true) {

                int id = 0;
                int productId = 0;
                int userid = 0;

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //SELECT sum(count) from quantity where productid = 21;
                Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM "
                        + dbHelper.TABLE_QUANTITY + " WHERE " + QUANTITY_2 + " = "
                        + item.getProductid(), null);

                if (cursor.moveToNext()) {

                    id = cursor.getInt(0);//to get id, 0 is the column index

                }

                //true
                addProduct(productModel.getProductid(), productModel.getTitle(),productModel.getDetails(),productModel.getPrice(),productModel.getOurprice(),
                        productModel.getOffer(),productModel.getIsavailable(),productModel.getSubcategory(),productModel.getImagename(),productModel.getIsactive(),
                        productModel.getCreated(),productModel.getCreatedby(),productModel.getModified(),
                        productModel.getModifiedby(),productModel.getRowCount(), prefManager.getUSER_ID());

            } else {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else
        {
            Toast.makeText(ProductDetailsActivity.this,"Go to Cart",Toast.LENGTH_LONG).show();
        }
    }

    private void addProduct(int Productid, String title,String Details,double Price,double Ourprice,
                            int Offer,int Isavailable,int Subcategory,String imgpath,int isactive,
                            String  created, int createdby,String modified,int modifiedby,int RowCount, int userid) {


        boolean isInserted = dbHelper.
                insertProductInfo(
                        Productid, title, Details, Price, Ourprice,
                        Offer, Subcategory, imgpath, Isavailable,
                        isactive, created, createdby, modified, modifiedby, RowCount, userid);

        if (isInserted == true) {

            int id = 0;
            int productId = 0;


            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //SELECT sum(count) from quantity where productid = 21;
            Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_PRODUCT, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

            if (cursor.moveToNext()) {

                id = cursor.getInt(0);//to get id, 0 is the column index
                productId = cursor.getInt(1);
                title = cursor.getString(2);
            }


            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);

            Toast.makeText(ProductDetailsActivity.this, "Added to Cart "+id, Toast.LENGTH_LONG).show();
            btn_addCart.setText("GO TO CART");
            Log.d("ProductviewListAdapter", "productAdapter " + title +" "+productId);
            //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

        } else {
            Toast.makeText(ProductDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        //}


    }


    private void getImages(int productid) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //Add porogressDialog
                Log.e("CheckReponseVideostry", "Called");
                ProductServices.getInstance(getApplicationContext()).
                        fetchProductImages(productid,
                                new ApiStatusCallBack<ArrayList<ProductImageModel>>() {

                                    @Override
                                    public void onSuccess(ArrayList<ProductImageModel> arraylist) {
                                        Log.e("CheckReponseVideosSucs", "Called");
                                        progressDialog.dismiss();
                                        slider(arraylist);
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Log.e("CheckReponseanError", ""+anError.getMessage());
                                        progressDialog.dismiss();
                                        Utility.showErrorMessage(ProductDetailsActivity.this, "Sorry ! Images not Available .." , Snackbar.LENGTH_LONG);
                                    }

                                    @Override
                                    public void onUnknownError(Exception e) {
                                        Log.e("CheckReponseUnknown", "Called");
                                        progressDialog.dismiss();
                                        //   Utility.showErrorMessage(getActivity(), e.getMessage(), Snackbar.LENGTH_LONG);
                                    }
                                });
            } else {
                Utility.showErrorMessage(ProductDetailsActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        }catch (Exception ex) {
            Log.e("CheckReponseOther", "Called");
            Log.e("GetVideoPackages","InsideGetVideoPackagesExtra"+ex);
            Utility.showErrorMessage(ProductDetailsActivity.this, "No record found", Snackbar.LENGTH_LONG);
        }
    }

    private  void getCount()
    {

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM " + dbHelper.TABLE_QUANTITY +" WHERE "+ QUANTITY_2 , null);

        int id = 0;

        if(cursor.moveToNext()){

            id = cursor.getInt(0);//to get id, 0 is the column index
            //productId=cursor.getInt(1);
            //userid=cursor.getInt(1);
            //prefManager.setCOUNT_ID(id);
        }

        // int count = bookingSqliteOperations.GetBookings().size();
        badgeLayout.setCountWithAnimation(id);
        //Toast.makeText(activity,"count "+ id , Toast.LENGTH_LONG).show();

    }

    private void addWishlist(int Productid, String title, String Details, double Price, double Ourprice,
                             int Offer, int Isavailable, int Subcategory, String imgpath, int isactive,
                             String created, int createdby, String modified, int modifiedby, int RowCount, int userid) {

        if (dbHelper.alreadyExistWishlistEntry(Productid)) {

            //DELETE from wishlist where productid = 1;
            Log.d("ProductviewListAdapter", "productAdapter " + title + " " + Productid);

            deleteWishlistItem(Productid);
        } else {
            boolean isInserted = dbHelper.
                    insertWishlistInfo(
                            Productid, title, Details, Price, Ourprice,
                            Offer, Subcategory, imgpath, Isavailable,
                            isactive, created, createdby, modified, modifiedby, RowCount, userid);
            Log.d("ProductviewListAdapter1", "productAdapter " + title + " " + Productid);

            if (isInserted == true) {

                int id = 0;
                int productId = 0;


                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //SELECT sum(count) from quantity where productid = 21;
                Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_PRODUCT, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

                if (cursor.moveToNext()) {

                    id = cursor.getInt(0);//to get id, 0 is the column index
                    productId = cursor.getInt(1);
                    title = cursor.getString(2);
                }

                Toast.makeText(ProductDetailsActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                iv_wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                Log.d("ProductviewListAdapter", "productAdapter " + title + " " + productId);
                //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

            } else {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void deleteWishlistItem(int productid) {

        //progressDialog.dismiss();
        Integer deletedRow = dbHelper.deleteWishlistItem(productid);

        if (deletedRow > 0){
            Toast.makeText(ProductDetailsActivity.this, "Removed from Wishlist !", Toast.LENGTH_SHORT).show();
            iv_wishlist.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
            //clear();
        } else {
            Toast.makeText(ProductDetailsActivity.this, "Wishlist is Empty !", Toast.LENGTH_SHORT).show();
        }
    }

    public void slider(ArrayList<ProductImageModel> arraylist) {
        progressDialog.dismiss();
        HashMap<String, String> url_maps1 = new HashMap<String, String>();
        int id = 1;
        for (ProductImageModel model : arraylist) {
            Log.e("checkLists",""+arraylist);

            String imgPath = Constants.URL_PRODUCT_IMG +model.getImagename();
            //String imgPath = model.getImages();
            //Log.e( "slider: ",imgPath);
            url_maps1.put(imgPath,imgPath);
        }
        for (String name : url_maps1.keySet()) {
            //Log.e("checkImg",""+name);
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.image(url_maps1.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(defaultSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);//Fade
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        // mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.AuthUserActivity;
import com.teammandroid.dairyapplication.activities.ProductDetailsActivity;
import com.teammandroid.dairyapplication.admin.activities.AddProductActivity;
import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.activities.ProductListActivity;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.CUST_16;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.CUST_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_1;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_PRODUCT;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_QUANTITY;
import static com.teammandroid.dairyapplication.utils.Constants.URL_PRODUCT_IMG;


public class ProductViewListAdapter extends RecyclerView.Adapter<ProductViewListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProductModel>   HoldersArrayList;
    private  ItemClickListener itemClickListener;
    String categoryName;
    private Activity activity;
    ProgressDialog progressDialog;

    ProductModel expenseCategoryHolder;
    DatabaseHelper dbHelper;
    PrefManager prefManager;

    public ProductViewListAdapter(Activity activity, ArrayList<ProductModel>   HoldersArrayList,
                                  ItemClickListener itemClickListener){

        this.activity = activity;
        this.HoldersArrayList =   HoldersArrayList;
        this.expenseCategoryHolder=expenseCategoryHolder;
        this.itemClickListener=itemClickListener;
    }


    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_product, null);

        progressDialog = new ProgressDialog(activity);
        dbHelper = new DatabaseHelper(activity);
        prefManager = new PrefManager(activity);

        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel item = HoldersArrayList.get(position);

        //holder.ll_add.setVisibility(View.VISIBLE);

        Log.e("CategoryListAdapter"," "+item.toString());
        holder.tv_title.setText(HoldersArrayList.get(position).getTitle());
        holder.tv_amount.setText(String.valueOf(HoldersArrayList.get(position).getDetails()));
        holder.tv_ourprice.setText(String.valueOf(HoldersArrayList.get(position).getOurprice()));
        holder.tv_ActualPrice.setText(String.valueOf(HoldersArrayList.get(position).getPrice()));
        holder.tv_offer.setText(String.valueOf(HoldersArrayList.get(position).getOffer()));
        //holder.tv_reason.setText("Date: "+ExpenseHoldersArrayList.get(position).getDetails());
        Picasso.with(activity)
                .load(URL_PRODUCT_IMG + item.getImagename())
                .into(holder.img_title);

        holder.iv_add.setVisibility(View.GONE);

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*    Bundle bundle=new Bundle();
                   bundle.putParcelable("ProductModel", item);
                   Utility.launchActivity(activity, EditCategoryActivity.class, true, bundle);
                */
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("categoryName",categoryName);
                bundle.putParcelable("ProductModel", item);
                Utility.launchActivity(activity, ProductListActivity.class, false);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("CatAdapter ",item.getProductid()+" "+item.getImagename()+" "+item.getTitle()+" "+item.getDetails());
                dialogForDeleteImage(item.getProductid(),item.getTitle(), item.getDetails(),item.getPrice(),
                        item.getOurprice(),item.getOffer(),item.getIsavailable(),item.getSubcategory(),
                        item.getImagename());

            }
        });

        holder.rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("ProductModel", item);
                Utility.launchActivity(activity, ProductDetailsActivity.class, false,bundle);

            }
        });

        if (dbHelper.alreadyExistProductEntry(item.getProductid(), prefManager.getUSER_ID()))
        {
            holder.btn_add.setVisibility(View.GONE);
            holder.btn_allreadyadded.setVisibility(View.VISIBLE);
            holder.btn_allreadyadded.setText("Check cart");
        }
        else {
            holder.btn_allreadyadded.setVisibility(View.GONE);
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                /*addProduct(item.getProductid(), item.getTitle(),item.getDetails(),item.getPrice(),item.getOurprice(),
                        item.getOffer(),item.getIsavailable(),item.getSubcategory(),item.getImagename(),item.getIsactive(),
                        item.getCreated(),item.getCreatedby(),item.getModified(),item.getModifiedby(),item.getRowCount());
*/
                    if (validateUser()) {

                        addQuantity(item);
                    }
                    else {
                        Utility.launchActivity(activity, AuthUserActivity.class, false);//,bundle);

                    }

                    //deleteQuantity(holder.totalview);
                }
            });

        }


        if (dbHelper.alreadyExistWishlistEntry(item.getProductid())) {

            holder.btn_add_wish.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }
        else {

            holder.btn_add_wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CartAdapter ", item.getProductid() + " " + item.getImagename() + " " + item.getTitle() + " " + item.getDetails());
                /*addWishlist(item.getProductid(), item.getTitle(), item.getDetails(), item.getPrice(), item.getOurprice(),
                        item.getOffer(), item.getIsavailable(), item.getSubcategory(), item.getImagename(), item.getIsactive(),
                        item.getCreated(), item.getCreatedby(), item.getModified(),
                        item.getModifiedby(), item.getRowCount(), prefManager.getUSER_ID());*/

                    if (validateUser()) {

                        addWishlist(item.getProductid(), item.getTitle(), item.getDetails(), item.getPrice(), item.getOurprice(),
                                item.getOffer(), item.getIsavailable(), item.getSubcategory(), item.getImagename(), item.getIsactive(),
                                item.getCreated(), item.getCreatedby(), item.getModified(),
                                item.getModifiedby(), item.getRowCount(), prefManager.getUSER_ID(),holder.btn_add_wish);
                    } else {
                        Utility.launchActivity(activity, AuthUserActivity.class, false);//,bundle);

                    }   }
            });



        }
    }

    @Override
    public int getItemCount() {
        return   HoldersArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title, tv_amount, tv_reason,totalview;
        TextView tv_ourprice, tv_ActualPrice, tv_offer;
        ImageView img_title;
        LinearLayout ll_add;
        ImageView iv_edit,iv_delete,iv_add,iv_remove,iv_addImg;
        Button btn_add, btn_allreadyadded;
        RelativeLayout rl_category;
        ImageView btn_add_wish;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_offer = (TextView) itemView.findViewById(R.id.tv_offer);
            tv_ActualPrice = (TextView) itemView.findViewById(R.id.tv_ActualPrice);
            tv_ourprice = (TextView) itemView.findViewById(R.id.tv_ourprice);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_reason = (TextView) itemView.findViewById(R.id.tv_reason);
            totalview = (TextView) itemView.findViewById(R.id.totalview);
            iv_edit =  itemView.findViewById(R.id.iv_edit);
            iv_delete =  itemView.findViewById(R.id.iv_delete);
            iv_add =  itemView.findViewById(R.id.iv_add);
            iv_remove =  itemView.findViewById(R.id.iv_remove);
            iv_addImg =  itemView.findViewById(R.id.iv_addImg);
            ll_add =  itemView.findViewById(R.id.ll_add);
            btn_add =  itemView.findViewById(R.id.btn_add);
            btn_allreadyadded =  itemView.findViewById(R.id.btn_allreadyadded);
            btn_add_wish =  itemView.findViewById(R.id.btn_add_wish);
            rl_category =  itemView.findViewById(R.id.rl_category);

            img_title =  itemView.findViewById(R.id.img_title);
            itemView.setOnClickListener(this); // bind the listener

            tv_ActualPrice.setPaintFlags(tv_ActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<ProductModel> newList) {
        HoldersArrayList = new ArrayList<>();
        HoldersArrayList.addAll(newList);
        notifyDataSetChanged();
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

            activity.finish();
            activity.overridePendingTransition( 0, 0);
            activity.startActivity(activity.getIntent());
            activity.overridePendingTransition( 0, 0);

            Log.d("ProductviewListAdapter", "productAdapter " + title +" "+productId);
            //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        //}


    }

    private void addQuantity(ProductModel item)
    {

        if (!dbHelper.alreadyExistProductEntry(item.getProductid(),prefManager.getUSER_ID()))
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
                addProduct(item.getProductid(), item.getTitle(),item.getDetails(),item.getPrice(),item.getOurprice(),
                        item.getOffer(),item.getIsavailable(),item.getSubcategory(),item.getImagename(),item.getIsactive(),
                        item.getCreated(),item.getCreatedby(),item.getModified(),
                        item.getModifiedby(),item.getRowCount(), prefManager.getUSER_ID());

                Toast.makeText(activity, "Added to Cart ", Toast.LENGTH_LONG).show();




            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }

        else
        {
            Toast.makeText(activity,"Go to Cart",Toast.LENGTH_LONG).show();
        }
    }

    private void dialogForDeleteImage(int pid,String title,String Details,double Price,
                                      double Ourprice,int Offer,int Isavailable, int Subcategory,String imgpath) {
        // this.correct = correct;
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.delete_subject_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_title =  resultbox.findViewById(R.id.text_title);
        text_title.setText(" Are you sure you want to delete ? ");
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(activity)) {
                    /**********Delete Code****************/
                    AndroidNetworking.upload(Constants.URL_Product)
                            // .addFileToUpload("", "certificate") //Adding file
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "0")
                            .addMultipartParameter("Productid", String.valueOf(pid))
                            .addMultipartParameter("Title", title)
                            .addMultipartParameter("Details",Details)
                            .addMultipartParameter("Price", String.valueOf(Price))
                            .addMultipartParameter("Ourprice", String.valueOf(Ourprice))
                            .addMultipartParameter("Offer", String.valueOf(Offer))
                            .addMultipartParameter("Isavailable", String.valueOf(Isavailable))
                            .addMultipartParameter("Subcategory", String.valueOf(Subcategory))
                            .addMultipartParameter("Image", imgpath)
                            //.addMultipartParameter("Imagename", "path")
                            .addMultipartParameter("LogedinUserId", "1")


                            .setTag("uploadTest")
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    resultbox.cancel();
                                    Log.e("adapter1", "uploadImage: totalBytes: " + totalBytes);
                                    Log.e("adapter1", "uploadImage: bytesUploaded: " + bytesUploaded);
                                    progressDialog.setMessage("Deleting File, Please wait...");
                                    progressDialog.show();
                                    progressDialog.setCancelable(false);
                                }
                            })
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.hide();
                                    Log.e("adapter1", "FileonRes: " + response.toString());
                                    //Toast.makeText(activity,"Deleted Succesfully ",Toast.LENGTH_LONG).show();
                                    //Intent intent= new Intent(activity,AdvertisementListActivity.class);
                                    //startActivity(intent);
                                    //rl_img_delete.setVisibility(View.GONE);
                                    resultbox.cancel();
                                    activity.finish();
                                    activity.overridePendingTransition( 0, 0);
                                    activity.startActivity(activity.getIntent());
                                    activity.overridePendingTransition( 0, 0);
                                }

                                @Override
                                public void onError(ANError error) {
                                    resultbox.cancel();
                                    Log.e("adapter1", "FileonError: ", error);
                                    progressDialog.hide();
                                }
                            });
                } else {
                    resultbox.cancel();
                    Toast.makeText(activity, "No Internet Connection",Toast.LENGTH_SHORT).show();                }
            }

            ;

            /**************************************/
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

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
            SessionHelper sessionHelper=new SessionHelper(activity);
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

    private void addWishlist(int Productid, String title, String Details, double Price, double Ourprice,
                             int Offer, int Isavailable, int Subcategory, String imgpath, int isactive,
                             String created, int createdby, String modified, int modifiedby, int RowCount, int userid, ImageView btn_add_wish) {

        if (dbHelper.alreadyExistWishlistEntry(Productid)) {

            //DELETE from wishlist where productid = 1;
            Log.d("ProductviewListAdapter", "productAdapter " + title + " " + Productid);
            deleteWishlistItem(Productid,btn_add_wish);
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

               /* activity.finish();
                activity.overridePendingTransition( 0, 0);
                activity.startActivity(activity.getIntent());
                activity.overridePendingTransition( 0, 0);*/

                Toast.makeText(activity, "Added to wishlist", Toast.LENGTH_SHORT).show();

                btn_add_wish.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

                Log.d("ProductviewListAdapter", "productAdapter " + title + " " + productId);
                //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteWishlistItem(int productid, ImageView btn_add_wish) {

        //progressDialog.dismiss();
        Integer deletedRow = dbHelper.deleteWishlistItem(productid);

        if (deletedRow > 0){
            btn_add_wish.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
            Toast.makeText(activity, "Removed from Wishlist !", Toast.LENGTH_SHORT).show();
            //clear();
        } else {
            Toast.makeText(activity, "Wishlist is Empty !", Toast.LENGTH_SHORT).show();
        }
    }


}

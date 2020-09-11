package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.AddSubCategoryActivity;
import com.teammandroid.dairyapplication.admin.activities.EditCategoryActivity;
import com.teammandroid.dairyapplication.admin.activities.EditSubcategoryActivity;
import com.teammandroid.dairyapplication.admin.activities.ProductListActivity;
import com.teammandroid.dairyapplication.admin.activities.SubcategoryListActivity;
import com.teammandroid.dairyapplication.admin.model.SubcategoryModel;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.teammandroid.dairyapplication.utils.Constants.URL_CATEGORY_IMG;
import static com.teammandroid.dairyapplication.utils.Constants.URL_SUB_CATEGORY_IMG;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<SubcategoryModel>   ExpenseHoldersArrayList;
    private  ItemClickListener itemClickListener;
    String categoryName;
    private Activity activity;
    ProgressDialog progressDialog;

    SubcategoryModel expenseCategoryHolder;

    public SubCategoryAdapter(Activity activity, ArrayList<SubcategoryModel>   ExpenseHoldersArrayList,
                              ItemClickListener itemClickListener){

        this.activity = activity;
        this.ExpenseHoldersArrayList =   ExpenseHoldersArrayList;
        this.expenseCategoryHolder=expenseCategoryHolder;
        this.itemClickListener=itemClickListener;
    }


    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_category_grid, null);
        progressDialog = new ProgressDialog(activity);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubcategoryModel item = ExpenseHoldersArrayList.get(position);

        //Log.e("subCategoryListAdapter"," "+categoryName);
        holder.tv_title.setText(ExpenseHoldersArrayList.get(position).getTitle());
        holder.tv_amount.setText(String.valueOf(ExpenseHoldersArrayList.get(position).getDetails()));
        //holder.tv_reason.setText("Date: "+ExpenseHoldersArrayList.get(position).getDetails());
        Picasso.with(activity)
                .load(URL_SUB_CATEGORY_IMG+item.getImagename())
                .into(holder.img_title);


       holder.iv_edit.setVisibility(View.GONE);
       holder.iv_delete.setVisibility(View.GONE);

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Bundle bundle=new Bundle();
                //   bundle.putString("categoryName",categoryName);
                   bundle.putParcelable("SubcategoryModel", item);
                   Utility.launchActivity(activity, EditSubcategoryActivity.class, true, bundle);
            }
        });

        holder.cv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                //   bundle.putString("categoryName",categoryName);
                bundle.putParcelable("SubcategoryModel", item);
                Utility.launchActivity(activity, ProductListActivity.class, false,bundle);

            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            Log.d("CatAdapter ",item.getSubcategoryid()+" "+item.getCategoryid()+" "+item.getImagename()+" "+item.getTitle()+" "+item.getDetails());
                dialogForDeleteImage(item.getSubcategoryid(),item.getCategoryid(),item.getImagename(),item.getTitle(),item.getDetails());

            }
        });


    }

    @Override
    public int getItemCount() {
        return   ExpenseHoldersArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title, tv_amount, tv_reason,tv_day;
        ImageView img_title;
        ImageView iv_edit,iv_delete,iv_add;
        CardView cv_list;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_reason = (TextView) itemView.findViewById(R.id.tv_reason);
            iv_edit =  itemView.findViewById(R.id.iv_edit);
            iv_delete =  itemView.findViewById(R.id.iv_delete);
            iv_add =  itemView.findViewById(R.id.iv_add);
            cv_list =  itemView.findViewById(R.id.cv_list);

            img_title =  itemView.findViewById(R.id.img_title);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<SubcategoryModel> newList) {
          ExpenseHoldersArrayList = new ArrayList<>();
          ExpenseHoldersArrayList.addAll(newList);
        notifyDataSetChanged();
    }


    private void dialogForDeleteImage(int Subid,int id,String imgpath,String Title ,String Details ) {
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

                    AndroidNetworking.upload(Constants.URL_SUB_CATEGORY)
                            // .addFileToUpload("", "certificate") //Adding file
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "0")
                            .addMultipartParameter("Subcategoryid", String.valueOf(Subid))
                            .addMultipartParameter("Categoryid", String.valueOf(id))
                            .addMultipartParameter("Title", Title)
                            .addMultipartParameter("Details",Details)
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




}

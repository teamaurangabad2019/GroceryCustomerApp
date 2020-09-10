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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.Network.OrderServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyOrderListActivity;
import com.teammandroid.dairyapplication.admin.model.DeliveryboyStatusModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DeliveryboyOrderAdapter extends RecyclerView.Adapter<DeliveryboyOrderAdapter.MyViewHolder> {

    private static final String TAG = "DeliveryboyListAdapter";
    private LayoutInflater inflater;
    private ArrayList<ProductModel> userModelArrayList;
    private  ItemClickListener itemClickListener;
    private Activity activity;
    ProgressDialog progressDialog;

    int userId, teacherId;

        public DeliveryboyOrderAdapter(Activity activity, ArrayList<ProductModel> userModelArrayList, ItemClickListener itemClickListener){

        this.activity = activity;
        this.userModelArrayList =   userModelArrayList;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);

        progressDialog = new ProgressDialog(activity);
            return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        ProductModel item = userModelArrayList.get(position);

        Log.d("adpaterCart" ,item.getDetails());

        viewHolder.mTitle.setText(item.getTitle());
        viewHolder.mDesp.setText(item.getDetails());
        viewHolder.mOffer.setText("Rs " + item.getPrice());
        viewHolder.mOfferprice.setText(String.valueOf("("+item.getOffer() + "% off ) "));
        viewHolder.mPrice.setText(String.valueOf(item.getOurprice()));
        viewHolder.txt_total_amount.setText(String.valueOf(item.getOurprice()));
        //viewHolder.txt_quantity.setText(String.valueOf(item.getRowCount()));
    }

    @Override
    public int getItemCount() {
        return   userModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView iv_cart,iv_remove,iv_addImg;
        RelativeLayout rl_delete;
        int count = 0;
        TextView mTitle, mDesp, mOfferprice, mOffer, mPrice, txt_quantity, txt_total_amount, btn_booknow;
        TextView totalview;

        public MyViewHolder(View itemView) {
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
            iv_remove = itemView.findViewById(R.id.iv_remove);
            totalview = itemView.findViewById(R.id.totalview);
            iv_addImg = itemView.findViewById(R.id.iv_addImg);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)


    public void setFilter(ArrayList<ProductModel> newList) {
        // ClassModelsArrayList =newList;
        userModelArrayList = new ArrayList<>();
        userModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }



}

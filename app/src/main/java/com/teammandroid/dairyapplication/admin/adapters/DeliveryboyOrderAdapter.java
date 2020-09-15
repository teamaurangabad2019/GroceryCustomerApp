package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Paint;
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
import com.squareup.picasso.Picasso;
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

import static com.teammandroid.dairyapplication.utils.Constants.URL_CATEGORY_IMG;
import static com.teammandroid.dairyapplication.utils.Constants.URL_PRODUCT_IMG;


public class DeliveryboyOrderAdapter extends RecyclerView.Adapter<DeliveryboyOrderAdapter.MyViewHolder> {

    private static final String TAG = "DeliveryboyListAdapter";
    private LayoutInflater inflater;
    private ArrayList<ProductModel> userModelArrayList;
    private  ItemClickListener itemClickListener;
    private Activity activity;
    ProgressDialog progressDialog;
    int status;
    int paymentMode;
    int userId, teacherId;

        public DeliveryboyOrderAdapter(Activity activity, ArrayList<ProductModel> userModelArrayList, int status, int paymentMode, ItemClickListener itemClickListener){

        this.activity = activity;
        this.userModelArrayList =   userModelArrayList;
        this.status =   status;
        this.paymentMode =   paymentMode;
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
        //viewHolder.mDesp.setText(item.getDetails());
        viewHolder.mOffer.setText("Rs " + item.getPrice());
        viewHolder.mOfferprice.setText(String.valueOf("("+item.getOffer() + "% off ) "));
        viewHolder.mPrice.setText(String.valueOf(item.getOurprice()));
        viewHolder.txt_total_amount.setText(String.valueOf(item.getOurprice()));
        viewHolder.tv_quantity.setText("Qty :"+String.valueOf(item.getQuantity()));
        //viewHolder.txt_quantity.setText(String.valueOf(item.getRowCount()));

        Picasso.with(activity)
                .load(URL_PRODUCT_IMG+item.getImagename())
                .into(viewHolder.img);

        if (paymentMode==0)
        {
            viewHolder.pay_type.setText("Cash");
        }
        else
        {
            viewHolder.pay_type.setText("Via card");
        }

        //0 pending
        // 1 approve
        //2 cancel

        if (status==0)
        {
            viewHolder.order_status.setText("Pending");
        }
        else if (status==1)
        {
            viewHolder.order_status.setText("Approved");
        }
        else if (status==2)
        {
            viewHolder.order_status.setText("Cancel");
        }
    }

    @Override
    public int getItemCount() {
        return   userModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView iv_cart,iv_remove,iv_addImg,img;
        RelativeLayout rl_delete;
        int count = 0;
        TextView mTitle, mDesp, mOfferprice, mOffer, mPrice, txt_quantity, txt_total_amount, btn_booknow;
        TextView pay_type,order_status;
        TextView totalview,tv_quantity;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            mOffer = itemView.findViewById(R.id.offer);
            mOfferprice = itemView.findViewById(R.id.offerprice);
            mPrice = itemView.findViewById(R.id.price);
            mTitle = itemView.findViewById(R.id.title);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            btn_booknow = itemView.findViewById(R.id.btn_booknow);
            iv_cart = itemView.findViewById(R.id.iv_cart);
            iv_remove = itemView.findViewById(R.id.iv_remove);
            totalview = itemView.findViewById(R.id.totalview);
            iv_addImg = itemView.findViewById(R.id.iv_addImg);
            pay_type = itemView.findViewById(R.id.pay_type);
            order_status = itemView.findViewById(R.id.order_status);
            itemView.setOnClickListener(this); // bind the listener

            mOfferprice.setPaintFlags(mOfferprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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

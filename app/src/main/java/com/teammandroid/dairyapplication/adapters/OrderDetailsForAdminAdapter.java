package com.teammandroid.dairyapplication.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.OrderModel;

import java.util.ArrayList;

public class OrderDetailsForAdminAdapter extends RecyclerView.Adapter<OrderDetailsForAdminAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity mContext;
    ArrayList<OrderModel> list;
    private  OrderDetailsForAdminAdapter.ItemClickListener itemClickListener;

    public OrderDetailsForAdminAdapter(Activity context, ArrayList<OrderModel> list) {
        this.list = list;
        mContext = context;
    }

    public OrderDetailsForAdminAdapter(Activity context, ArrayList<OrderModel> list,
                                       OrderDetailsForAdminAdapter.ItemClickListener itemClickListener){
        this.list = list;
        mContext = context;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override

    public  OrderDetailsForAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_for_order, null);

        //  prefManager=new PrefManager(mContext);
        return new  OrderDetailsForAdminAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final OrderModel item = list.get(i);

        viewHolder.tv_cust_name.setText("Order id: "+item.getOrderid());
        viewHolder.tv_quantity.setText("Total Price: "+ String.valueOf(item.getTotalprice()));
        viewHolder.tv_total_amount.setText("Saved Price: "+ String.valueOf(item.getSavedprice()));
        viewHolder.tv_address.setText("Address: "+item.getAddress());

        if(item.getStatus()==0)
        {
            viewHolder.tv_product_name.setText("Status: Pending");

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_cart;
        RelativeLayout rl_delete;
        int count = 0;
        TextView tv_cust_name, tv_product_name,tv_quantity,tv_total_amount, tv_address;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cust_name = itemView.findViewById(R.id.tv_cust_name);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            tv_address = itemView.findViewById(R.id.tv_address);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }


}


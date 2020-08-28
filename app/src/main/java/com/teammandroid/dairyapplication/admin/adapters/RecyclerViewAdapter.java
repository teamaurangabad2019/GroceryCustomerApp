package com.teammandroid.dairyapplication.admin.adapters;


import android.content.Context;
import android.graphics.Paint;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.teammandroid.dairyapplication.R;


import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";


    //vars
    private ArrayList<String> mOffersprice = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private ArrayList<String> mWeight = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mCutoff = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> offers, ArrayList<String> images, ArrayList<String> label, ArrayList<String> weight, ArrayList<String> price, ArrayList<String> cutoff) {
        mOffersprice = offers;
        mImage = images;
        mLabel = label;
        mWeight = weight;
        mPrice = price;
        mCutoff = cutoff;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: ");

        viewHolder.offers.setText(mOffersprice.get(i));

        Glide.with(mContext)
                .asBitmap()
                .load(mImage.get(i))
                .placeholder(R.drawable.ic_credit_card)
                .error(R.drawable.ic_credit_card)
                .into(viewHolder.items);
        viewHolder.label.setText(mLabel.get(i));
        viewHolder.weight.setText(mWeight.get(i));
        viewHolder.price.setText(mPrice.get(i));
        viewHolder.cutoff.setText(mCutoff.get(i));


    }


    @Override
    public int getItemCount() {

        return mOffersprice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView items;
        RecyclerView recyclerView;
        TextView label,weight,price,cutoff,offers;
        Button add;

        public ViewHolder(View itemView) {
            super(itemView);

            items= itemView.findViewById(R.id.iv_items);
            offers= itemView.findViewById(R.id.tv_offer);
            label= itemView.findViewById(R.id.tv_iv_label);
            weight= itemView.findViewById(R.id.tv_weight);
            price= itemView.findViewById(R.id.tv_price);
            cutoff= itemView.findViewById(R.id.tv_cutoff);
            add= itemView.findViewById(R.id.bt_add);
            recyclerView= itemView.findViewById(R.id.rv_products1);

            cutoff.setPaintFlags(cutoff.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }


    }
}




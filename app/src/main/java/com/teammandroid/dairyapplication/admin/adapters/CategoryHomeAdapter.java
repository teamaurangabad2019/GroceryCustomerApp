package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;

import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.activities.SubcategoryListActivity;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;

import com.teammandroid.dairyapplication.utils.Utility;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.teammandroid.dairyapplication.utils.Constants.URL_CATEGORY_IMG;
import static com.teammandroid.dairyapplication.utils.Constants.URL_PACKAGE;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity activity;
    ArrayList<CategoryModel> list;
    Date startDate,endDate;
    private  ItemClickListener itemClickListener;

    public CategoryHomeAdapter(Activity context, ArrayList<CategoryModel> list) {
        this.list = list;
        activity = context;
    }

    public CategoryHomeAdapter(Activity context, ArrayList<CategoryModel> list, ItemClickListener itemClickListener){
        this.list = list;
        activity = context;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override

    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_layout_xml, null);


        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final CategoryModel item = list.get(i);

        Log.e(TAG, list.toString());
        viewHolder.name.setText(item.getTitle());


      /*  Random r = new Random();
        int red=r.nextInt(255 - 0 + 1)+0;
        int green=r.nextInt(255 - 0 + 1)+0;
        int blue=r.nextInt(255 - 0 + 1)+0;

        GradientDrawable draw = new GradientDrawable();
        //draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.rgb(red,green,blue));
       // viewHolder.rl_package.setBackground(draw);*/

        Picasso.with(activity)
                .load(URL_CATEGORY_IMG+item.getImagename())
                .into(viewHolder.image_view);

        viewHolder.rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                //   bundle.putString("categoryName",categoryName);
                bundle.putParcelable("CategoryModel", item);
                Utility.launchActivity(activity, SubcategoryListActivity.class, false,bundle);

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_view;
        TextView name;
        RelativeLayout rl_category;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image_view = itemView.findViewById(R.id.image_view);
            rl_category = itemView.findViewById(R.id.rl_category);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<CategoryModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}



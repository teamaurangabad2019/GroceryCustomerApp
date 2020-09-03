package com.teammandroid.dairyapplication.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.PrefManager;

import java.util.ArrayList;

public class CustomerDetailsAdapter extends RecyclerView.Adapter<CustomerDetailsAdapter.MyViewHolder> {

    private static final String TAG = "CustomerDetailsAdapter";

    private Activity mContext;
    ArrayList<UserModel> list;
    private CustomerDetailsAdapter.ItemClickListener itemClickListener;
  //  private ViewPager viewPager;
    int selectedPosition=-1;
    PrefManager prefManager;


    public CustomerDetailsAdapter(Activity context, ArrayList<UserModel> list) {
        this.list = list;
        mContext = context;
    }

    public CustomerDetailsAdapter(Activity context, ArrayList<UserModel> list, CustomerDetailsAdapter.ItemClickListener itemClickListener) {
        this.list = list;
        mContext = context;
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override

    public CustomerDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_for_customer, null);
       // viewPager = (ViewPager) mContext.findViewById(R.id.pager);
        //  prefManager=new PrefManager(mContext);
        return new CustomerDetailsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final UserModel item = list.get(i);

        prefManager=new PrefManager(mContext);
        viewHolder.tv_title.setText("Name: " + item.getFullname());
        viewHolder.tv_price.setText("Address: " + item.getAddress());
        viewHolder.tv_mobile.setText("Mobile: " + item.getMobile());
        viewHolder.tv_username.setText("Username: " + item.getFullname());
        viewHolder.tv_password.setText("Password: " + item.getMobile());


       // Toast.makeText(mContext, "pppp  "+i, Toast.LENGTH_SHORT).show();

        if(selectedPosition==i) {
            viewHolder.rlayout1.setBackgroundColor(Color.parseColor("#5E6872"));
        }
        else {
            viewHolder.rlayout1.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefManager.setD_BOY_ID(item.getUserid());

                selectedPosition = i;
                notifyDataSetChanged();
            }
        });


        int user_id = item.getUserid();

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Called");

              //  showCustomDialogForPlaceVacation(i, user_id);

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_cart, img_delete;
        RelativeLayout rl_delete;
        int count = 0;
        TextView tv_title, tv_price, tv_mobile, tv_username, tv_password;
        CardView card_view;
        RelativeLayout rlayout1;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            img_delete = itemView.findViewById(R.id.img_delete);
            card_view = itemView.findViewById(R.id.card_view);
            rlayout1 = itemView.findViewById(R.id.rlayout1);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_password = itemView.findViewById(R.id.tv_password);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<UserModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }


/*
    private void showCustomDialogForPlaceVacation(int i, int u_id) {
        // this.correct = correct;
        final Dialog resultbox = new Dialog(mContext);
        resultbox.setContentView(R.layout.delete_user_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(mContext)) {

                    */
/**********Delete Code****************//*


                    try {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("type", 1);
                            jsonObject.put("Action", 0);
                            jsonObject.put("NewuserId", u_id);

                        } catch (Exception e) {
                            Log.e("JSONOBJECTerr", "" + e);
                            Utility.showErrorMessage(mContext, "Something went wrong", Snackbar.LENGTH_SHORT);
                        }


                        AndroidNetworking.post(Constants.USER_REG)
                                .addJSONObjectBody(jsonObject)
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {


                                        try {
                                            TypeToken<UserRegResponse> token = new TypeToken<UserRegResponse>() {
                                            };
                                            UserRegResponse userModels = new Gson().fromJson(response.toString(), token.getType());
                                            Log.e("UserModel", "" + userModels);

                                            if (userModels.getHasError() == 1) {
                                                //progressDialog.dismiss();
                                                Utility.showErrorMessage(mContext, "Something went wrong", Snackbar.LENGTH_SHORT);
                                                Log.e("1111", "1111");
                                            } else {
                                                list.remove(i);
                                                notifyDataSetChanged();
                                                notifyItemRemoved(i);
                                                notifyItemRangeChanged(i, list.size());
                                                Utility.showErrorMessage(mContext, "Deleted Successfully", Snackbar.LENGTH_SHORT);
                                                viewPager.setCurrentItem(viewPager.getCurrentItem());
                                                Log.e("1111", "2222");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("error", e.getMessage());
                                        }

                                        resultbox.cancel();


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Log.e("NotesPkgs:anError", "" + anError);
                                        Log.e("NotesPkgs:anError", "" + anError.getErrorBody());
                                        Utility.showErrorMessage(mContext, "Something went wrong", Snackbar.LENGTH_SHORT);
                                    }
                                });
                    } catch (Exception ex) {
                        Log.e("onUnknownError", "" + ex);
                        Utility.showErrorMessage(mContext, "Something went wrong", Snackbar.LENGTH_SHORT);
                    }

                } else {
                    Utility.showErrorMessage(mContext, "No Internet Connection", Snackbar.LENGTH_SHORT);
                }
            }

            ;

            */
/**************************************//*

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }
*/


}


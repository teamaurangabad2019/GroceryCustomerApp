package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;

import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DeliveryboyStatusAdapter extends RecyclerView.Adapter<DeliveryboyStatusAdapter.MyViewHolder> {

    private static final String TAG = "DeliveryboyListAdapter";
    private LayoutInflater inflater;
    private ArrayList<DeliveryboyStatusModel> userModelArrayList;
    private  ItemClickListener itemClickListener;
    private Activity activity;
    ProgressDialog progressDialog;

    int userId, teacherId;

        public DeliveryboyStatusAdapter(Activity activity, ArrayList<DeliveryboyStatusModel> userModelArrayList, ItemClickListener itemClickListener){

        this.activity = activity;
        this.userModelArrayList =   userModelArrayList;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_order, null);

        progressDialog = new ProgressDialog(activity);
            return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

       DeliveryboyStatusModel item = userModelArrayList.get(position);
       holder.tv_name.setText(userModelArrayList.get(position).getFullname());
       holder.tv_address.setText(userModelArrayList.get(position).getAddress());
       holder.tv_mob.setText(userModelArrayList.get(position).getMobile());
       if (userModelArrayList.get(position).getPaymentmode()==0)
       {
           holder.tv_paymentmode.setText("Cash");
       }
       else
       {
           holder.tv_paymentmode.setText("Cash");
       }
       holder.tv_savePrice.setText("Saved Price : "+String.valueOf(userModelArrayList.get(position).getSavedprice()));
       holder.tv_ActualPrice.setText("Total Price : "+String.valueOf(userModelArrayList.get(position).getTotalprice()));
       //holder.tv_approved.setText(userModelArrayList.get(position).getDeliverydate());

        //0 pending
        // 1 approve
        //2 cancel

        if (item.getStatus()==0)
        {
            holder.iv_staus1.setBackgroundResource(R.drawable.ic_check);
            holder.iv_staus2.setBackgroundResource(R.drawable.ic_shopping_basket);
            holder.iv_staus3.setBackgroundResource(R.drawable.ic_shopping_basket);
            //holder.tv_pending.setTextColor(Color.parseColor("#00933C"));
           // holder.tv_pending.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }else if (item.getStatus()==1)
        {
            //holder.tv_pending.setTextColor(Color.parseColor("#00933C"));
            holder.iv_staus1.setBackgroundResource(R.drawable.ic_check);
            holder.iv_staus2.setBackgroundResource(R.drawable.ic_check);
            holder.iv_staus3.setBackgroundResource(R.drawable.ic_shopping_basket);
            //holder.tv_pending.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }else if (item.getStatus()==2)
        {
           // holder.tv_pending.setTextColor(Color.parseColor("#00933C"));
            holder.iv_staus1.setBackgroundResource(R.drawable.ic_check);
            holder.iv_staus2.setBackgroundResource(R.drawable.ic_check);
            holder.iv_staus3.setBackgroundResource(R.drawable.ic_check);
           // holder.tv_pending.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }

       holder.iv_update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             Toast.makeText(activity,"edit",Toast.LENGTH_SHORT).show();
               updateDialog(item,0);
           }
       });


        holder.iv_order.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Bundle bundle = new Bundle();
                   Toast.makeText(activity,"click "+item.getOrderid(),Toast.LENGTH_SHORT).show();

                   bundle.putParcelable("DeliveryboyStatusModel",item);
                   Log.d("AapterStatus"," "+item.getOrderid());
                   Utility.launchActivity(activity, DeliveryboyOrderListActivity.class,false,bundle);
               }
           });
    }

    @Override
    public int getItemCount() {
        return   userModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_pending, tv_cancel, tv_approved;
        TextView tv_name, tv_address, tv_mob,tv_paymentmode,tv_savePrice,tv_ActualPrice;
        CircleImageView img_title;
        CardView cv_customerItem;
        ImageView iv_staus1,iv_staus2,iv_staus3,iv_update,iv_order;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_ActualPrice = (TextView) itemView.findViewById(R.id.tv_ActualPrice);
            tv_savePrice = (TextView) itemView.findViewById(R.id.tv_savePrice);
            tv_paymentmode = (TextView) itemView.findViewById(R.id.tv_paymentmode);
            tv_mob = (TextView) itemView.findViewById(R.id.tv_mob);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_pending = (TextView) itemView.findViewById(R.id.tv_pending);
            tv_cancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            tv_approved = (TextView) itemView.findViewById(R.id.tv_approved);
            cv_customerItem =  itemView.findViewById(R.id.cv_customerItem);
            iv_order =  itemView.findViewById(R.id.iv_order);
            iv_update =  itemView.findViewById(R.id.iv_update);
            iv_staus1 =  itemView.findViewById(R.id.iv_staus1);
            iv_staus2 =  itemView.findViewById(R.id.iv_staus2);
            iv_staus3 =  itemView.findViewById(R.id.iv_staus3);

            img_title =  itemView.findViewById(R.id.img_title);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)

    private void updateDialog(DeliveryboyStatusModel deliveryboyOrderModel, int status){
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.staus_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_resume = (Button) resultbox.findViewById(R.id.btn_resume);

        RadioGroup radioGroup = resultbox.findViewById(R.id.groupradio);
        RadioButton radio_id1 = resultbox.findViewById(R.id.radio_id1);
        RadioButton radio_id2 = resultbox.findViewById(R.id.radio_id2);
        RadioButton radio_id3 = resultbox.findViewById(R.id.radio_id3);

        radio_id1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                if (Utility.isNetworkAvailable(activity)) {

                    Log.e("statusAdapter", "orderId "+deliveryboyOrderModel.getOrderid()+" "+0+" "+deliveryboyOrderModel.getDeliveryboyid()
                                    +" "+deliveryboyOrderModel.getDeliverydate()+" "+deliveryboyOrderModel.getAddress()+" "+deliveryboyOrderModel.getPaymentmode()
                            +" "+deliveryboyOrderModel.getTotalprice()+" "+deliveryboyOrderModel.getSavedprice());

                        placeOrder(deliveryboyOrderModel.getOrderid(),deliveryboyOrderModel.getUserid(),0,deliveryboyOrderModel.getDeliveryboyid()
                                ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());

                }
            }
        });


        radio_id2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                if (Utility.isNetworkAvailable(activity)) {

                        placeOrder(deliveryboyOrderModel.getOrderid(),deliveryboyOrderModel.getUserid(),1,deliveryboyOrderModel.getDeliveryboyid()
                                ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());

                }
       }
        });


        radio_id3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                if (Utility.isNetworkAvailable(activity)) {

                        placeOrder(deliveryboyOrderModel.getOrderid(),deliveryboyOrderModel.getUserid(),2,deliveryboyOrderModel.getDeliveryboyid()
                                ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());

                }
       }
        });



       /* radio_id1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                if (Utility.isNetworkAvailable(activity)) {

               // Is the current Radio Button checked?
                boolean checked = ((RadioButton) v).isChecked();

                int status =0;
                switch(v.getId()){

                    case R.id.radio_id1:
                        if(checked)
                            status=0;

                        placeOrder(deliveryboyOrderModel.getOrderid(),status,deliveryboyOrderModel.getDeliveryboyid()
                                ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());
                        break;

                    case R.id.radio_id2:
                        if(checked)
                            status=1;
                        placeOrder(deliveryboyOrderModel.getOrderid(),status,deliveryboyOrderModel.getDeliveryboyid()
                                ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());

                        break;

                       case R.id.radio_id3:
                        if(checked)
                            status=2;
                           placeOrder(deliveryboyOrderModel.getOrderid(),status,deliveryboyOrderModel.getDeliveryboyid()
                                   ,deliveryboyOrderModel.getDeliverydate(),deliveryboyOrderModel.getAddress(),deliveryboyOrderModel.getPaymentmode()
                                   ,deliveryboyOrderModel.getTotalprice(),deliveryboyOrderModel.getSavedprice());

                           break;

                }


                } else {
                    Utility.showErrorMessage(activity, "No Internet Connection", Snackbar.LENGTH_SHORT);
                }
       }
        });*/

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }

    public void setFilter(ArrayList<DeliveryboyStatusModel> newList) {
        // ClassModelsArrayList =newList;
        userModelArrayList = new ArrayList<>();
        userModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    private void placeOrder(int Orderid,int userId,int Status,int Deliveryboyid,String Deliverydate,
                            String Address,int Paymentmode,double Totalprice,double Savedprice) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d("orderAdapter","status "+Status);

        OrderServices.getInstance(activity).
                createOrder(Orderid,userId,Status,Deliveryboyid,Deliverydate,
                        Address,Paymentmode,Totalprice,Savedprice, new ApiStatusCallBack<Response>() {
                            @Override
                            public void onSuccess(Response response) {
                                progressDialog.dismiss();
                                Log.e(TAG, "response " + response.getMessage());

                                activity.finish();
                                activity.overridePendingTransition( 0, 0);
                                activity.startActivity(activity.getIntent());
                                activity.overridePendingTransition( 0, 0);

                                //Utility.launchActivity(CartActivity.this,OrderHistoryActivity.class,true);
                                //Utility.launchActivity(CartActivity.this, EnquiryListActivity.class,false);
                                Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e(TAG, "ANError " + anError.getMessage());
                                Utility.showErrorMessage(activity, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                Utility.showErrorMessage(activity, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                        });

    }




}

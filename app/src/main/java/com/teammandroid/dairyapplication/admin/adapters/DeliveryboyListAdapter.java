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
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyListActivity;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyStatusListActivity;
import com.teammandroid.dairyapplication.admin.activities.EditDeliveryBoyActivity;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DeliveryboyListAdapter extends RecyclerView.Adapter<DeliveryboyListAdapter.MyViewHolder> {

    private static final String TAG = "DeliveryboyListAdapter";
    private LayoutInflater inflater;
    private ArrayList<UserModel> userModelArrayList;
    private  ItemClickListener itemClickListener;
    private Activity activity;
    ProgressDialog progressDialog;

    int userId, teacherId;

    public DeliveryboyListAdapter(Activity activity, ArrayList<UserModel> userModelArrayList, ItemClickListener itemClickListener){

        this.activity = activity;
        this.userModelArrayList =   userModelArrayList;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_user, null);

        progressDialog = new ProgressDialog(activity);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserModel item = userModelArrayList.get(position);
        holder.tv_title.setText(userModelArrayList.get(position).getFullname());
        holder.tv_mobile.setText(userModelArrayList.get(position).getMobile());

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("userModel",item);
                Utility.launchActivity(activity, EditDeliveryBoyActivity.class,true,bundle);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bundle bundle=new Bundle();
                // bundle.putParcelable("userModel",item);
                dialogForDeleteRecord(position,userModelArrayList,item.getUserid());
                // Log.e("DeleteButton ", String.valueOf(schoolModel.getSchoolid()+" "+item.getUserid()));
            }
        });

        holder.iv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("statusModel",item);
                Utility.launchActivity(activity, DeliveryboyStatusListActivity.class,false,bundle);
            }
        });



    }

    @Override
    public int getItemCount() {
        return   userModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title, tv_mobile, tv_teacher_specify,tv_day;
        CircleImageView img_title;
        CardView cv_customerItem;
        ImageView iv_edit,iv_delete,iv_order;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            tv_teacher_specify = (TextView) itemView.findViewById(R.id.tv_teacher_specify);
            cv_customerItem =  itemView.findViewById(R.id.cv_customerItem);
            iv_edit =  itemView.findViewById(R.id.iv_edit);
            iv_delete =  itemView.findViewById(R.id.iv_delete);
            iv_order =  itemView.findViewById(R.id.iv_order);

            img_title =  itemView.findViewById(R.id.img_title);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)

    private void dialogForDeleteRecord(int i, ArrayList<UserModel> userModels, int teacherId){
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.delete_teacher_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_resume = (Button) resultbox.findViewById(R.id.btn_resume);

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                Log.d("TeacherAdapterDI ",
                        "Teacher "+userId+" "+teacherId);

                //deleteTeacher(userId, 2,teacherId);

                if (Utility.isNetworkAvailable(activity)) {

                    /**********Delete Code****************/

                    AndroidNetworking.upload(Constants.URL_USER)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "0")
                            .addMultipartParameter("Userid", String.valueOf(userModels.get(0).getUserid()))
                            .addMultipartParameter("Fullname", userModels.get(0).getFullname())
                            .addMultipartParameter("Roleid","2")
                            .addMultipartParameter("Address", userModels.get(0).getAddress())
                            .addMultipartParameter("Mobile", userModels.get(0).getMobile())
                            .addMultipartParameter("Token", "token")
                            .addMultipartParameter("Email", userModels.get(0).getEmail())
                            .addMultipartParameter("Profilepic", String.valueOf(userModels.get(0).getProfilepic()))
                            .addMultipartParameter("Device", Utility.getDeviceName())
                            .addMultipartParameter("LogedinUserId", "0")
                            .setTag("uploadTest")
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    Log.e(TAG, "uploadImage: totalBytes: " + totalBytes);
                                    Log.e(TAG, "uploadImage: bytesUploaded: " + bytesUploaded);
                                }
                            })
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    com.google.common.reflect.TypeToken<Response> token = new TypeToken<Response>() {
                                    };
                                    progressDialog.dismiss();
                                    Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                    Utility.launchActivity(activity, DeliveryboyListActivity.class,true);
                                    Toast.makeText(activity, "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(activity, "Network error" + error.getMessage());
                                }
                            });

                } else {
                    Utility.showErrorMessage(activity, "No Internet Connection", Snackbar.LENGTH_SHORT);
                }
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }

    public void setFilter(ArrayList<UserModel> newList) {
        // ClassModelsArrayList =newList;
        userModelArrayList = new ArrayList<>();
        userModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

/*
    private void deleteTeacher(int Userid,int Action,int Associateid) {
    // AuthServices deleteServices=new AuthServices(activity);
        AuthServices.getInstance(activity).DeleteDetails(Userid,Action,Associateid,new ApiStatusCallBack<ArrayList<DeleteModel>>() {

                     @Override
                    public void onSuccess(ArrayList<DeleteModel> deleteModels) {
                        Log.e("ChkUserdetails:", String.valueOf(deleteModels.get(0).getTransactionId()));
                        Utility.launchActivity(activity, TeacherListActivity.class,true);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("DeleteANError:", anError.getMessage());
                        //Utility.launchActivity(activity, TeacherListActivity.class,true);
                        activity.finish();
                        activity.overridePendingTransition( 0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition( 0, 0);
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        Log.e("DeleteException:", e.getMessage());
                        Utility.launchActivity(activity, TeacherListActivity.class,true);
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }

                });

    }
*/


}

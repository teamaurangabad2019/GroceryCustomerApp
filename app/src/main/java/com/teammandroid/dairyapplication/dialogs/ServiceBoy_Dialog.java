package com.teammandroid.dairyapplication.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.Network.OrderServices;
import com.teammandroid.dairyapplication.Network.UserServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.OrderHistoryActivity;
import com.teammandroid.dairyapplication.adapters.CustomerDetailsAdapter;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.OrderModel;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

/**
 * Created by s on 8/20/2018.
 */

public class ServiceBoy_Dialog extends Dialog implements View.OnClickListener {

    public Activity activity;


    static TextView btn_start_exam;
    static RelativeLayout rl_start_exam;
    Bundle bundle;

    RecyclerView recycler_view_quizpackages;
    CustomerDetailsAdapter customerDetailsAdapter;
    ProgressDialog progressDialog;

    OrderModel orderModel;
    PrefManager prefManager;

    int Delivery_boy_id=0;


    public ServiceBoy_Dialog() {
        super(null);
    }
    public ServiceBoy_Dialog(Activity activity, OrderModel orderModel)  {
        super(activity);
        this.activity = activity;
        this.orderModel=orderModel;

      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.startexam_dialog);
        bindView();
        btnlistener();
        prefManager.setD_BOY_ID(0);
        GetQuizPackages(2);


        Log.e("requestDetailservice", String.valueOf(orderModel));
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.float_close);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dismiss();
            }
        });



    }



    @Override
    public void onClick(View v) {

        Log.e("onClick", "Clicked");

        switch (v.getId()) {

            case R.id.btn_start_exam:


                if (Utility.isNetworkAvailable(activity)) {

                   // if(Delivery_boy_id>0) {
                    if(prefManager.getD_BOY_ID()>0) {
                        //updateDeliveryPerson(Integer.parseInt(prefManager.getORDER_ID()), Delivery_boy_id);


                        Log.e("number", orderModel.getAddress() );
                       // Log.e("dnumber",  prefManager.getD_BOY_MOB_NO());
                        updateDeliveryBoy(orderModel.getOrderid(),orderModel.getStatus(),prefManager.getD_BOY_ID(),orderModel.getDeliverydate(),
                                orderModel.getAddress(),orderModel.getPaymentmode(),orderModel.getTotalprice(),orderModel.getSavedprice(),
                                orderModel.getDeliveryboyid() );
                        //Toast.makeText(activity, "did"+prefManager.getD_BOY_ID(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Select delivery boy...", Toast.LENGTH_SHORT).show();
                    }

                   }
                else {
                    Utility.showErrorMessage(activity, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                }

                break;

            case R.id.rl_start_exam:

                if (Utility.isNetworkAvailable(activity)) {


                    if(prefManager.getD_BOY_ID()>0) {

                        updateDeliveryBoy(orderModel.getOrderid(),orderModel.getStatus(),prefManager.getD_BOY_ID(),orderModel.getDeliverydate(),
                                orderModel.getAddress(),orderModel.getPaymentmode(),orderModel.getTotalprice(),orderModel.getSavedprice(),
                                orderModel.getDeliveryboyid());

                    }
                    else {
                        Toast.makeText(activity, "Select delivery boy...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Utility.showErrorMessage(activity, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                }

                break;
        }
    }

 //   {"type":1,"Action":1,"Orderid":0, "Status":1,"Deliveryboyid":1,"Deliverydate":"Deliverydate",
     //       "Address":"Address","Paymentmode":1,"Totalprice":1,"Savedprice":1, "LogedinUserId":1}

    void updateDeliveryBoy(int Orderid, int Status, int Deliveryboyid, String Deliverydate, String Address,
                           int Paymentmode, double Totalprice, double Savedprice,
                             int LogedinUserId)
    {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OrderServices.getInstance(activity).insertOrderDetails(
                Orderid ,Status,Deliveryboyid, Deliverydate, Address,
                Paymentmode, Totalprice, Savedprice, LogedinUserId,
                new ApiStatusCallBack<Response>() {
                    @Override
                    public void onSuccess(Response userModels) {
                        Log.e("ChkOrderdetails:",userModels.getMessage());
                        Log.e("ChkOrderdetails:",userModels.toString());

                        if (userModels.getHasError()==1) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, userModels.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Utility.showErrorMessage(activity, userModels.getMessage());

                           // Log.e("mobile", prefManager.getD_BOY_MOB_NO());
                            showCustomDialogForAddRecord();

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity,"Server Error", Snackbar.LENGTH_SHORT);
                    }
                });
    }

    private void showCustomDialogForAddRecord(){
        // this.correct = correct;
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.assign_message_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_resume = (Button) resultbox.findViewById(R.id.btn_resume);
        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utility.launchActivity(activity, OrderHistoryActivity.class, true);
                /*if(quizStatus.getIssubmitted()==1){
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("QuizLevel",quizLevel);
                    Utility.launchActivity(QuizzActivity.this, ResultCardActivity.class,true,bundle);
                }else{

                }*/
                // resultbox.cancel();
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utility.launchActivity(activity, OrderHistoryActivity.class, true);
            }
        });

        resultbox.show();
    }



    private void btnlistener() {
        btn_start_exam.setOnClickListener(this);
        rl_start_exam.setOnClickListener(this);
    }
    private void bindView() {
        btn_start_exam=findViewById(R.id.btn_start_exam);
        rl_start_exam=findViewById(R.id.rl_start_exam);

        recycler_view_quizpackages=findViewById(R.id.recycler_view_quizpackages);
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
    }

    private void GetQuizPackages(int Roleid) {
        try {
            if (Utility.isNetworkAvailable(activity)) {

                //lyt_progress_employees.setVisibility(View.VISIBLE);
                //lyt_progress_employees.setAlpha(1.0f);
                Log.e("CheckReponseVideos", "Called");

                UserServices.getInstance(activity).GetAllDeliveryBoyDetails
                        (Roleid,new ApiStatusCallBack<ArrayList<UserModel>>() {

                            @Override
                            public void onSuccess(ArrayList<UserModel> arraylist) {
                                // lyt_progress_employees.setVisibility(View.GONE);
                                BindAnnoucements(arraylist);
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_employees.setVisibility(View.GONE);
                                Utility.showErrorMessage(activity, "Server Problem:", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //lyt_progress_employees.setVisibility(View.GONE);
                                Utility.showErrorMessage(activity, e.getMessage(), Snackbar.LENGTH_LONG);
                            }
                        });
            } else {
                Utility.showErrorMessage(activity, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        } catch (Exception ex) {
            Utility.showErrorMessage(activity, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void BindAnnoucements(final ArrayList<UserModel> mQuizPackages) {
        try {
            //  lyt_progress_employees.setVisibility(View.GONE);
            // recycler_view_quizpackages.setVisibility(View.VISIBLE);

            activity.setTitle("QuizPackages (" + mQuizPackages.size() + ")");

            Utility.showErrorMessage(activity, "Bind list", Snackbar.LENGTH_LONG);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recycler_view_quizpackages.setLayoutManager(mLayoutManager);
            recycler_view_quizpackages.setItemAnimator(new DefaultItemAnimator());
            recycler_view_quizpackages.setHasFixedSize(true);

            customerDetailsAdapter = new CustomerDetailsAdapter(activity, mQuizPackages);

          /*  customerDetailsAdapter = new CustomerDetailsAdapter(activity, mQuizPackages,
                    new CustomerDetailsAdapter.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            Log.e("Customeritem", String.valueOf(mQuizPackages.get(position).getUserid()));

                            Delivery_boy_id=mQuizPackages.get(position).getUserid();
                            Toast.makeText(activity, "Serviceboy name: "+mQuizPackages.get(position).getFullname(), Toast.LENGTH_SHORT).show();

                            }
                    });*/
            recycler_view_quizpackages.setAdapter(customerDetailsAdapter);
            customerDetailsAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Utility.showErrorMessage(activity, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }





}

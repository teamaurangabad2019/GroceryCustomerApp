package com.teammandroid.dairyapplication.admin.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.Network.OrderServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.adapters.OrderDetailsForAdminAdapter;
import com.teammandroid.dairyapplication.dialogs.ServiceBoy_Dialog;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.OrderModel;
import com.teammandroid.dairyapplication.model.RequestDetailsForAdminModel;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recycler_view_quizpackages;
    OrderDetailsForAdminAdapter orderDetailsForAdminAdapter;
    ImageView back_about;
    PrefManager prefManager;


    RequestDetailsForAdminModel requestDetailsForAdminModel;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        bindView();
        btnlistener();

        //getRequest(4);
        getRequestForAdmin(4,0);

    }

    private void btnlistener() {
        back_about.setOnClickListener(this);
    }

    private void bindView() {

        // requestDetailsForAdminModel = getIntent().getParcelableExtra("requestDetailsForAdminModel");
        back_about = findViewById(R.id.back_about);

        recycler_view_quizpackages=findViewById(R.id.recycler_view_service_request);
        prefManager=new PrefManager(OrderHistoryActivity.this);
        progressDialog = new ProgressDialog(this);

        //  requestDetailsForAdminModel=  new RequestDetailsForAdminModel();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back_about:
                Utility.launchActivity(OrderHistoryActivity.this, HomepageActivity.class,false);
                break;
        }
    }


    private void getRequestForAdmin( int Action, int Deliveryboyid) {
        try {
            if (Utility.isNetworkAvailable(OrderHistoryActivity.this)) {

                //lyt_progress_employees.setVisibility(View.VISIBLE);
                //lyt_progress_employees.setAlpha(1.0f);
                Log.e("CheckReponseVideos", "Called");
                progressDialog.setTitle("Please Wait Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                OrderServices.getInstance(OrderHistoryActivity.this).fetchOrder
                        (Action, Deliveryboyid,new ApiStatusCallBack<ArrayList<OrderModel>>() {
                            @Override
                            public void onSuccess(ArrayList<OrderModel> arraylist) {
                                Log.e("ChkDetail",arraylist.toString());

                                BindAnnoucementsForAdmin(arraylist);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_employees.setVisibility(View.GONE);
                                Log.e("ANError",anError.toString());
                                progressDialog.dismiss();
                                Utility.showErrorMessage(OrderHistoryActivity.this, "No record found", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //lyt_progress_employees.setVisibility(View.GONE);
                                Log.e("ANError",e.toString());
                                progressDialog.dismiss();
                                Utility.showErrorMessage(OrderHistoryActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);
                            }
                        });
            } else {
                Utility.showErrorMessage(OrderHistoryActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        } catch (Exception ex) {
            Utility.showErrorMessage(OrderHistoryActivity.this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void BindAnnoucementsForAdmin(final ArrayList<OrderModel> mQuizPackages) {
        try {

            OrderHistoryActivity.this.setTitle("QuizPackages (" + mQuizPackages.size() + ")");

            // Utility.showErrorMessage(DailyServiceRequestHistoryActivity.this, "Bind list", Snackbar.LENGTH_LONG);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderHistoryActivity.this);
            recycler_view_quizpackages.setLayoutManager(mLayoutManager);
            recycler_view_quizpackages.setItemAnimator(new DefaultItemAnimator());
            recycler_view_quizpackages.setHasFixedSize(true);

            orderDetailsForAdminAdapter = new OrderDetailsForAdminAdapter(OrderHistoryActivity.this,
                    mQuizPackages,
                    new OrderDetailsForAdminAdapter.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                       /*     OrderModel requestDetailsForAdminModel=mQuizPackages.get(position);

                            final ServiceBoy_Dialog dialog = new ServiceBoy_Dialog(OrderHistoryActivity.this,
                                    requestDetailsForAdminModel);
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(true);
*/
                        }
                    });
            recycler_view_quizpackages.setAdapter(orderDetailsForAdminAdapter);

            orderDetailsForAdminAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.e("ANErrorex",ex.toString());
            //  Utility.showErrorMessage(DailyServiceRequestHistoryActivity.this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }
}


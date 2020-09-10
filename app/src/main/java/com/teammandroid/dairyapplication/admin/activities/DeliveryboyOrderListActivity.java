package com.teammandroid.dairyapplication.admin.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teammandroid.dairyapplication.Network.OrderProductServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.admin.adapters.DeliveryboyOrderAdapter;
import com.teammandroid.dairyapplication.admin.adapters.DeliveryboyStatusAdapter;
import com.teammandroid.dairyapplication.admin.model.DeliveryboyStatusModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

public class DeliveryboyOrderListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG=DeliveryboyOrderListActivity.class.getSimpleName();

    ProgressDialog progressDialog;
    private ArrayList<ProductModel> mList = new ArrayList<>();
    RecyclerView rv_stafflist;

    Dialog resultbox;
    DeliveryboyOrderAdapter teacherListAdapter;
    private FloatingActionButton fb_create;

    ImageView iv_backprofile;
    ImageView img_openDrawer;
    TextView tv_toolbar_title;
    TextView txt_logout;
    PrefManager prefManager;

    DeliveryboyStatusModel deliveryboyStatusModel;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryboy_list);
        initView();

        fb_create.setVisibility(View.GONE);

        tv_toolbar_title.setText("Product List");


            img_openDrawer.setVisibility(View.VISIBLE);

            deliveryboyStatusModel=getIntent().getParcelableExtra("DeliveryboyStatusModel");
            getOrderList(deliveryboyStatusModel.getOrderid());

            Log.d(TAG, "DB dbID  " + prefManager.getUSER_ID());

    }

    private void getOrderList(int orderId) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                // progressDialog.setProgressStyle(R.id.abbreviationsBar);
                progressDialog.show();

                OrderProductServices.getInstance(getApplicationContext()).
                        fetchProductUsingOrderId(orderId,new ApiStatusCallBack<ArrayList<ProductModel>>() {
                            @Override
                            public void onSuccess(ArrayList<ProductModel> userModels) {

                                progressDialog.dismiss();
                                mList = userModels;
                                BindList(userModels);
                                //user = response.get(0);

                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Log.e("ANErrorTeacher", anError.getMessage());
                                showCustomDialogNoRecord();
                                //Utility.showErrorMessage(AssignSubjecttoTeacherActivity.this, "anError " + anError.getMessage());
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Utility.showErrorMessage(DeliveryboyOrderListActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(DeliveryboyOrderListActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(DeliveryboyOrderListActivity.this, ex.getMessage());
        }
    }

    private void BindList(final ArrayList<ProductModel> mUserList) {
        try {
            rv_stafflist.setVisibility(View.VISIBLE);


            Log.e("itemTeacher", String.valueOf(mUserList));
            //Utility.showErrorMessage(this, "Bind list", Snackbar.LENGTH_LONG);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DeliveryboyOrderListActivity.this);
            rv_stafflist.setLayoutManager(mLayoutManager);
            rv_stafflist.setItemAnimator(new DefaultItemAnimator());
            rv_stafflist.setHasFixedSize(true);

            teacherListAdapter = new DeliveryboyOrderAdapter(DeliveryboyOrderListActivity.this, mUserList,
                    new DeliveryboyOrderAdapter.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            // Bundle bundle=new Bundle();
                            //  bundle.putParcelable("DeliveryboyStatusModel",String.valueOf(mUserList));
                            //  Utility.launchActivity(DeliveryboyListActivity.this,EditTeachingStaffActivity.class,false,bundle);
                            //  Log.e("item", String.valueOf(mNotesPackages.get(position).getArrayList().get(position).getUsername()));

                        }
                    });
            rv_stafflist.setAdapter(teacherListAdapter);
            teacherListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            //Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void showCustomDialogNoRecord() {
        // this.correct = correct;
        resultbox = new Dialog(DeliveryboyOrderListActivity.this);
        resultbox.setContentView(R.layout.custom_dialog);
        resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_assign = resultbox.findViewById(R.id.text_title);

        text_assign.setText(R.string.text_dialog);

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
                onBackPressed();
                // Toast.makeText(getApplicationContext(),"DialogunPaidFinishBtn",Toast.LENGTH_LONG).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultbox.cancel();
                finish();
                onBackPressed();
            }
        });

        resultbox.show();
    }


    private void initView() {
        fb_create = (FloatingActionButton) findViewById(R.id.fb_create);
        fb_create.setOnClickListener(this);

        iv_backprofile =  findViewById(R.id.iv_backprofile);
        iv_backprofile.setOnClickListener(this);




        img_openDrawer =  findViewById(R.id.img_openDrawer);
        img_openDrawer.setOnClickListener(this);


        txt_logout =  findViewById(R.id.txt_logout);
        txt_logout.setOnClickListener(this);

        tv_toolbar_title =  findViewById(R.id.tv_toolbar_title);
        prefManager = new PrefManager(DeliveryboyOrderListActivity.this);

        rv_stafflist = findViewById(R.id.rv_stafflist);
        progressDialog = new ProgressDialog(DeliveryboyOrderListActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.fb_create:
                Utility.launchActivity(DeliveryboyOrderListActivity.this,AddDeliveryboyActivity.class,false);
                break;

            case R.id.iv_backprofile:
                onBackPressed();
                break;


            case R.id.txt_logout:
                showCustomDialogLogout();
                break;

        }
    }

    private void showCustomDialogLogout() {
        // this.correct = correct;
        resultbox = new Dialog(DeliveryboyOrderListActivity.this);
        resultbox.setContentView(R.layout.custom_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_assign = resultbox.findViewById(R.id.text_title);

        text_assign.setText("Are you sure you want to exit ?");

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                SessionHelper sessionManager=new SessionHelper(DeliveryboyOrderListActivity.this);
                sessionManager.logoutUser();
                prefManager.setROLE_ID(5);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultbox.cancel();
            }
        });

        resultbox.show();
    }

}

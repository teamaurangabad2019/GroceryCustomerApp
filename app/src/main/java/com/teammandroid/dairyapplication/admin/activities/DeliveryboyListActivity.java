package com.teammandroid.dairyapplication.admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teammandroid.dairyapplication.Network.UserServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.adapters.DeliveryboyListAdapter;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

public class DeliveryboyListActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    private ArrayList<UserModel> mList = new ArrayList<>();
    RecyclerView rv_stafflist;

    Dialog resultbox;
    DeliveryboyListAdapter teacherListAdapter;
    private FloatingActionButton fb_create;

    ImageView iv_backprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryboy_list);
        initView();

        rv_stafflist = findViewById(R.id.rv_stafflist);
        progressDialog = new ProgressDialog(DeliveryboyListActivity.this);
        getStaffList();
    }

    private void getStaffList() {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                // progressDialog.setProgressStyle(R.id.abbreviationsBar);
                progressDialog.show();

                UserServices.getInstance(getApplicationContext()).
                        getDeliveryBoy(new ApiStatusCallBack<ArrayList<UserModel>>() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> userModels) {

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
                                Utility.showErrorMessage(DeliveryboyListActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(DeliveryboyListActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(DeliveryboyListActivity.this, ex.getMessage());
        }
    }

    private void BindList(final ArrayList<UserModel> mUserList) {
        try {
            rv_stafflist.setVisibility(View.VISIBLE);


            Log.e("itemTeacher", String.valueOf(mUserList));
            //Utility.showErrorMessage(this, "Bind list", Snackbar.LENGTH_LONG);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DeliveryboyListActivity.this);
            rv_stafflist.setLayoutManager(mLayoutManager);
            rv_stafflist.setItemAnimator(new DefaultItemAnimator());
            rv_stafflist.setHasFixedSize(true);

            teacherListAdapter = new DeliveryboyListAdapter(DeliveryboyListActivity.this, mUserList,
                    new DeliveryboyListAdapter.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            // Bundle bundle=new Bundle();
                            //  bundle.putParcelable("userModel",String.valueOf(mUserList));
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
        resultbox = new Dialog(DeliveryboyListActivity.this);
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.fb_create:
                Utility.launchActivity(DeliveryboyListActivity.this,AddDeliveryboyActivity.class,false);
                break;

            case R.id.iv_backprofile:
                onBackPressed();
                break;
        }
    }
}

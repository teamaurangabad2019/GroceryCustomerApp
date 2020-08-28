package com.teammandroid.dairyapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.chaos.view.PinView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuhart.stepview.StepView;
import com.teammandroid.dairyapplication.Network.AuthServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;


public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    Activity activity;
    private int currentStep = 0;
    LinearLayout layout1, layout2, layout3, lyt_progress_auth;
    StepView stepView;

    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60 * 60 * 1000;


    private String OTP = "123456";
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;
    private Button button3;
    private ProgressBar pbLoading;
    private TextView tv_nickname_status;

    private EditText phoneNum;
    private PinView verifyCodeET;
    private TextView phonenumberText;

    private String mVerificationId;
    String mobileNumber = null;
    String token="";
    private UserModel userModel;
    String phoneNumber ;

    ProgressDialog progressDialog;
    PrefManager prefManager;
    SessionHelper sessionHelper;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        activity = AuthenticationActivity.this;
        prefManager=new PrefManager(activity);
        pbLoading = new ProgressBar(activity);
        sessionHelper = new SessionHelper(AuthenticationActivity.this);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        lyt_progress_auth = (LinearLayout) findViewById(R.id.lyt_progress_auth);
        sendCodeButton = (Button) findViewById(R.id.submit1);
        verifyCodeButton = (Button) findViewById(R.id.submit2);
        button3 = (Button) findViewById(R.id.submit3);
        phoneNum = (EditText) findViewById(R.id.phonenumber);
        progressDialog =new ProgressDialog(AuthenticationActivity.this);
        verifyCodeET = (PinView) findViewById(R.id.pinView);
        phonenumberText = (TextView) findViewById(R.id.phonenumberText);
        tv_nickname_status = (TextView) findViewById(R.id.tv_nickname_status);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
        layout1.setVisibility(View.VISIBLE);

        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    tv_nickname_status.setVisibility(View.GONE);
                    String name = String.valueOf(s);
                    if (name.isEmpty() || s.equals("") || s == "" || name == "") {
                        tv_nickname_status.setVisibility(View.GONE);
                    } else {
                        //  GetNumber(name);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onTextChanged: ", e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber = phoneNum.getText().toString();
                phonenumberText.setText(phoneNumber);
                String deviceName = Utility.getDeviceName();
                Log.e("BChkNumber", phoneNumber);

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNum.setError("Enter a Phone Number");
                    phoneNum.requestFocus();
                } else if (phoneNumber.length() < 10) {
                    phoneNum.setError("Please enter a valid phone");
                    phoneNum.requestFocus();
                }  else {
                    if (currentStep < stepView.getStepCount() - 1) {
                        currentStep++;
                        stepView.go(currentStep, true);
                    } else {
                        stepView.done(true);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                }
            }
        });

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String verificationCode = verifyCodeET.getText().toString();
                if (verificationCode.isEmpty()) {
                    Toast.makeText(AuthenticationActivity.this, "Enter verification code", Toast.LENGTH_SHORT).show();
                } else {

                    if (verificationCode.equals(OTP)) {
                        Log.d(TAG, "signInWithCredential:success");

                        if (currentStep < stepView.getStepCount() - 1) {
                            currentStep++;
                            stepView.go(currentStep, true);
                        } else {
                            stepView.done(true);
                        }
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                    } else {
                        Utility.showErrorMessage(activity, "Something went wrong");
                        Toast.makeText(AuthenticationActivity.this, "Something  wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                } else {
                    stepView.done(true);
                }
                layout3.setVisibility(View.GONE);
                lyt_progress_auth.setVisibility(View.VISIBLE);
                lyt_progress_auth.setVisibility(View.GONE);

                // Utility.launchActivity(AuthenticationActivity.this, HomepageActivity.class, true);
                Log.e( "createUser: ","phe" +phoneNumber);
                createUser(phoneNumber);
                //Toast.makeText(AuthenticationActivity.this,"ContinuePressed " ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String phoneNumber)
    {
        Log.e( "createUser: ","phone" +phoneNumber);
        try {

            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //Creating a multi part request
                AndroidNetworking.upload(Constants.URL_LOGIN)
                        .addMultipartParameter("type", "1")
                        .addMultipartParameter("Action", "1")
                        .addMultipartParameter("Userid", "0")
                        .addMultipartParameter("Fullname", "")
                        .addMultipartParameter("Roleid", "2")
                        .addMultipartParameter("Address", "")
                        .addMultipartParameter("Mobile", phoneNumber)
                        .addMultipartParameter("Token", "")
                        .addMultipartParameter("Email", "")
                        .addMultipartParameter("Profilepic", "")
                        .addMultipartParameter("Device", Utility.getDeviceName())
                        .addMultipartParameter("LogedinUserId", "0")  //.setNotificationConfig(new UploadNotificationConfig())
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
                                TypeToken<Response> token = new TypeToken<Response>() {
                                };
                                progressDialog.dismiss();
                                Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                //Utility.launchActivity(AuthenticationActivity.this, ProfileActivity.class,true);
                                Toast.makeText(getApplicationContext(), "createSuccess" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onResponseAuth " + response1.getMessage());

                                //imp
                                prefManager.setUSER_ID(response1.getResult());
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                //  progressDialog.dismiss();
                                if (response1.getMessage().endsWith("already exist")) {
                                    //get and update
                                    GetUser(response1.getResult(), false);
                                } else {
                                    //only get
                                    GetUser(response1.getResult(), true);
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.e(TAG, "onResponseUser " + error.getMessage());
                                progressDialog.dismiss();
                                // Toast.makeText(getApplicationContext(), "ANErrorCreate" + error.getMessage(), Toast.LENGTH_LONG).show();
                                Utility.showErrorMessage(AuthenticationActivity.this, "Network error" + error.getMessage());
                            }
                        });
            }
            else {
                Utility.showErrorMessage(AuthenticationActivity.this, "Could not connect to the internet");
            }
        } catch (Exception exc) {
            progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(), "excCreate" + exc.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("getMessage", exc.getMessage());
            //  Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void GetUser(int UserId, final boolean is_new) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Log.e(TAG, "GetUser: " + UserId);
                AuthServices.getInstance(getApplicationContext()).
                        GetUserDetails(UserId, new ApiStatusCallBack<ArrayList<UserModel>>() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> response) {
                                progressDialog.dismiss();
                                // lyt_progress_reg.setVisibility(View.GONE);
                                userModel = response.get(0);
                                sessionHelper.setLogin(true);
                                prefManager.setROLE_ID(2);
                                Toast.makeText(AuthenticationActivity.this,"Success "+userModel.getUserid() ,Toast.LENGTH_SHORT).show();

                                if (is_new) {
                                    progressDialog.dismiss();
                                    sessionHelper.createLoginSession(userModel);
                                    Log.e(TAG, "isNew: " + userModel.getUserid() +"Mob "+userModel.getMobile());
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("userData",userModel);
                                    bundle.putString("number", userModel.getMobile());
                                    Utility.launchActivity(AuthenticationActivity.this, ProfileActivity.class, true,bundle);
                                } else {
                                    Log.e(TAG, "olreadyExist: " + userModel.getUserid());
                                    UpdateUser();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                // Toast.makeText(AuthenticationActivity.this,"anErrorA "+anError.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthenticationActivity.this, "Invalid Credentials");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                // Toast.makeText(AuthenticationActivity.this,"eA "+e.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthenticationActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(AuthenticationActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(AuthenticationActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(AuthenticationActivity.this, ex.getMessage());
        }
    }

    private void UpdateUser() {
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {
                //Creating a multi part request
                AndroidNetworking.upload(Constants.URL_LOGIN)
                        .addMultipartParameter("type", "1")
                        .addMultipartParameter("Action", "1")
                        .addMultipartParameter("Userid", String.valueOf(userModel.getUserid()))
                        .addMultipartParameter("Fullname",userModel.getFullname() )
                        .addMultipartParameter("Roleid", String.valueOf(userModel.getRoleid()))
                        .addMultipartParameter("Address", userModel.getAddress())
                        .addMultipartParameter("Mobile", phoneNumber)
                        .addMultipartParameter("Token", token)
                        .addMultipartParameter("Email", userModel.getEmail())
                        .addMultipartParameter("Profilepic", userModel.getProfilepic())
                        .addMultipartParameter("Device", Utility.getDeviceName())
                        .addMultipartParameter("LogedinUserId", String.valueOf(userModel.getUserid()))
                        //.setNotificationConfig(new UploadNotificationConfig())
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
                                TypeToken<Response> token = new TypeToken<Response>() {
                                };
                                Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                Log.e(TAG, "uploadImage:onResponse: " + response);

                                progressDialog.dismiss();
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                sessionHelper.createLoginSession(userModel);
                                Log.e(TAG, "onSuccess in Update: " + userModel.getUserid());
                                Utility.showErrorMessage(AuthenticationActivity.this,response.toString());
                                Toast.makeText(AuthenticationActivity.this,"response1 "+response1.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.launchActivity(AuthenticationActivity.this, HomepageActivity.class, true);
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.e(TAG, "onErrorUpdate: ", error);
                                progressDialog.dismiss();
                                // Toast.makeText(AuthenticationActivity.this,"ANError "+error.getMessage(),Toast.LENGTH_SHORT).show();
                                //   lyt_progress_reg.setVisibility(View.GONE);
                                Utility.showErrorMessage(AuthenticationActivity.this, error.getMessage(), Snackbar.LENGTH_SHORT);

                            }
                        });
            } catch (Exception exc) {
                progressDialog.dismiss();
                Log.e("getMessage", exc.getMessage());
                Utility.showErrorMessage(AuthenticationActivity.this,exc.getMessage());
                // Toast.makeText(AuthenticationActivity.this,"exc "+exc.getMessage(),Toast.LENGTH_SHORT).show();

                //  Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Utility.showErrorMessage(AuthenticationActivity.this, "Could not connect to the internet");
        }
    }

}


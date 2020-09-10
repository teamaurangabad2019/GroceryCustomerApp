package com.teammandroid.dairyapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import com.teammandroid.dairyapplication.Network.OTPServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.activities.SelectRoleActivity;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AuthUserActivity extends AppCompatActivity {

    private static final String TAG = AuthUserActivity.class.getSimpleName();

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
    private TextView tv_sms_recv,text_otp_expire;

    private String mVerificationId;
    String mobileNumber = null;
    String token="";
    private UserModel userModel;
    String phoneNumber ;

    ProgressDialog progressDialog;
    PrefManager prefManager;
    SessionHelper sessionHelper;

    int roleIdBundle;
    TextView tv_regFree,tv_verify;

    DatabaseHelper dbHelper;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;

    String message = "Thank you for visiting Grocery Store \n Your OTP :" + OTP;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        activity = AuthUserActivity.this;
        prefManager=new PrefManager(activity);
        pbLoading = new ProgressBar(activity);
        sessionHelper = new SessionHelper(AuthUserActivity.this);
        dbHelper = new DatabaseHelper(AuthUserActivity.this);

        roleIdBundle=getIntent().getIntExtra("roleId",0);
        Log.e(TAG,"roleIDBundle "+roleIdBundle);

        tv_sms_recv = findViewById(R.id.tv_sms_recv);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        lyt_progress_auth = (LinearLayout) findViewById(R.id.lyt_progress_auth);
        sendCodeButton = (Button) findViewById(R.id.submit1);
        verifyCodeButton = (Button) findViewById(R.id.submit2);
        button3 = (Button) findViewById(R.id.submit3);
        phoneNum = (EditText) findViewById(R.id.phonenumber);
        progressDialog =new ProgressDialog(AuthUserActivity.this);
        verifyCodeET = (PinView) findViewById(R.id.pinView);
        phonenumberText = (TextView) findViewById(R.id.phonenumberText);
        tv_nickname_status = (TextView) findViewById(R.id.tv_nickname_status);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);

        tv_verify = findViewById(R.id.tv_verify);
        text_otp_expire = findViewById(R.id.text_otp_expire);

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

                prefManager.setAUTH_BACK(2);
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
                    if (Utility.isNetworkAvailable(getApplicationContext())) {

                        timerForOtp(mobileNumber,message);
                        //createUser(phoneNumber);

                    /*
                        int count = 0;
                        if (currentStep < step_view.getStepCount() - 1) {

                            //timerForOtp(mobileNumber,message);

                            currentStep++;
                            step_view.go(currentStep, true);
                        } *//*else {
                            step_view.done(true);
                        }*/
                    }

                    else {
                        Utility.showErrorMessage(AuthUserActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    tv_verify.setVisibility(View.GONE);
                }
            }
        });

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String verificationCode = verifyCodeET.getText().toString();
                if (verificationCode.isEmpty()) {
                    Toast.makeText(AuthUserActivity.this, "Enter verification code", Toast.LENGTH_SHORT).show();
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
                        tv_verify.setText("Verified");
                        tv_verify.setVisibility(View.VISIBLE);
                        //prefManager.setROLE_ID(6);
                        prefManager.setAUTH_BACK(3);
                    } else {
                        Utility.showErrorMessage(activity, "Something went wrong");
                        Toast.makeText(AuthUserActivity.this, "Something  wrong", Toast.LENGTH_SHORT).show();
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

                AndroidNetworking.upload(Constants.URL_USER)
                        .addMultipartParameter("type", "1")
                        .addMultipartParameter("Action", "1")
                        .addMultipartParameter("Userid", "0")
                        .addMultipartParameter("Fullname", "")
                        .addMultipartParameter("Roleid", "3")
                        .addMultipartParameter("Address", "")
                        .addMultipartParameter("Mobile", phoneNumber)
                        .addMultipartParameter("Token", "")
                        .addMultipartParameter("Email", "")
                        .addMultipartParameter("Profilepic", "")
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
                                TypeToken<Response> token = new TypeToken<Response>() {
                                };
                                progressDialog.dismiss();
                                Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                //Utility.launchActivity(AuthenticationActivity.this, EntryProfileActivity.class,true);
                                Toast.makeText(getApplicationContext(), "createSuccess" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onResponseAuth " + response1.getMessage());

                                //imp
                                prefManager.setUSER_ID(response1.getResult());
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                //  progressDialog.dismiss();
                                //  updateUser(response1.getResult());
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
                                Utility.showErrorMessage(AuthUserActivity.this, "Network error" + error.getMessage());
                            }
                        });
            }
            else {
                Utility.showErrorMessage(AuthUserActivity.this, "Could not connect to the internet");
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
                                //prefManager.setROLE_ID(2);

                                if (is_new) {
                                    progressDialog.dismiss();
                                    sessionHelper.createLoginSession(userModel);
                                    prefManager.setaddress(userModel.getAddress());
                                    Log.e(TAG, "isNew: " + userModel.getUserid() +"Mob "+userModel.getMobile());
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("userData",userModel);
                                    bundle.putString("number", userModel.getMobile());
                                    Utility.launchActivity(AuthUserActivity.this, EntryProfileActivity.class, true,bundle);
                                    //layout1.setVisibility(View.GONE);
                                    //layout2.setVisibility(View.VISIBLE);
                                    //tv_verify.setVisibility(View.GONE);

                                    //Log.e(TAG, "PrinciSuccess "+OTP);
                                    //String message = "Thank you for visiting Sarvodaya International School \n Your OTP :" + OTP;
                                    //timerForOtp(mobileNumber,message);
                                } else {
                                    Log.e(TAG, "olreadyExist: " + userModel.getUserid() +"roleId "+prefManager.getROLE_ID());
                                    UpdateUser();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                // Toast.makeText(AuthenticationActivity.this,"anErrorA "+anError.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthUserActivity.this, "Invalid Credentials");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                // Toast.makeText(AuthenticationActivity.this,"eA "+e.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthUserActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(AuthUserActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(AuthenticationActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(AuthUserActivity.this, ex.getMessage());
        }
    }

    private void UpdateUser() {
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {
                //Creating a multi part request
                AndroidNetworking.upload(Constants.URL_USER)
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
                                Toast.makeText(AuthUserActivity.this,"response1 "+response1.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.launchActivity(AuthUserActivity.this, EntryProfileActivity.class, true);
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.e(TAG, "onErrorUpdate: ", error);
                                progressDialog.dismiss();
                                // Toast.makeText(AuthenticationActivity.this,"ANError "+error.getMessage(),Toast.LENGTH_SHORT).show();
                                //   lyt_progress_reg.setVisibility(View.GONE);
                                Utility.showErrorMessage(AuthUserActivity.this, error.getMessage(), Snackbar.LENGTH_SHORT);

                            }
                        });
            } catch (Exception exc) {
                progressDialog.dismiss();
                Log.e("getMessage", exc.getMessage());
                Utility.showErrorMessage(AuthUserActivity.this,exc.getMessage());
                // Toast.makeText(AuthenticationActivity.this,"exc "+exc.getMessage(),Toast.LENGTH_SHORT).show();

                //  Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Utility.showErrorMessage(AuthUserActivity.this, "Could not connect to the internet");
        }
    }

    private void adminCheck(String phoneNumber) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d("phoneNumber","phoneNumber "+phoneNumber);

        AuthServices.getInstance(activity).
                adminLogin(phoneNumber, new ApiStatusCallBack<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        progressDialog.dismiss();
                        Log.e(TAG, "response " + response.getMessage());

                        if (response.getResult() == 0)
                        {
                            //Invalid credential
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            layout1.setVisibility(View.GONE);
                            layout2.setVisibility(View.VISIBLE);

                            Log.e(TAG, "TeacherSuccess "+OTP);
                            String message = "Thank you for visiting Grocery Store \n Your OTP :" + OTP;
                            timerForOtp(mobileNumber,message);
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                            //Utility.launchActivity(AuthenticationActivity.this, HomepageActivity.class,false);
                        }

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

    private void getUserInfo(int UserId, final boolean is_new) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Log.e(TAG, "GetUser: " + UserId);
                AuthServices.getInstance(getApplicationContext()).
                        getUserInfo(UserId, new ApiStatusCallBack<ArrayList<UserModel>>() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> response) {
                                progressDialog.dismiss();
                                // lyt_progress_reg.setVisibility(View.GONE);
                                userModel = response.get(0);
                                sessionHelper.setLogin(true);
                                prefManager.setROLE_ID(2);

                                if (is_new) {
                                    progressDialog.dismiss();
                                    sessionHelper.createLoginSession(userModel);
                                    Log.e(TAG, "isNew: " + userModel.getUserid() +"Mob "+userModel.getMobile());
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("userData",userModel);
                                    bundle.putString("number", userModel.getMobile());
                                    //Utility.launchActivity(AuthenticationActivity.this, DeliveryboyStatusListActivity.class, true,bundle);
                                    layout1.setVisibility(View.GONE);
                                    layout2.setVisibility(View.VISIBLE);
                                    /*tv_verify.setVisibility(View.GONE);

                                    Log.e(TAG, "PrinciSuccess "+OTP);
                                    String message = "Thank you for visiting Sarvodaya International School \n Your OTP :" + OTP;
                                    timerForOtp(mobileNumber,message);*/
                                } else {
                                    Log.e(TAG, "olreadyExist: " + userModel.getUserid() +"roleId "+prefManager.getROLE_ID());
                                    UpdateUser();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                // Toast.makeText(AuthenticationActivity.this,"anErrorA "+anError.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthUserActivity.this, "Invalid Credentials");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                //  lyt_progress_reg.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                // Toast.makeText(AuthenticationActivity.this,"eA "+e.getMessage() ,Toast.LENGTH_SHORT).show();
                                Utility.showErrorMessage(AuthUserActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(AuthUserActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(AuthenticationActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(AuthUserActivity.this, ex.getMessage());
        }
    }
    // OTP Timer
    private void timerForOtp(String mobileNumber,String message) {

        prefManager.setAUTH_BACK(2);
        tv_sms_recv.setVisibility(View.GONE);
        SpannableString span = new SpannableString("Didn't receive SMS ? Resend");

        span.setSpan(new ForegroundColorSpan(Color.GRAY), 21, 27, 0);
        tv_sms_recv.setTextColor(Color.parseColor("#48494b"));

        mCountDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText(millisUntilFinished);
                //120 sec=120000ms
                if ((millisUntilFinished / 1000) == 117) {

                    //sendOTP(mobileNumber, message);
                    Log.e(TAG, "text " + message);

                }
                else {
                    Log.e(TAG, "seconds remaining: " + millisUntilFinished / 1000);
                    sendCodeButton.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() {
                Log.e(TAG,"Done! " );

                //sendOTP(mobileNumber, message);
                //Toast.makeText(AuthenticationActivity.this,"Time Out!! Resend OTP",Toast.LENGTH_LONG).show();
                SpannableString span = new SpannableString("Didn't receive SMS ? Resend");
                span.setSpan(new ForegroundColorSpan(Color.BLUE), 21, 27, 0);
                tv_sms_recv.setText(span, TextView.BufferType.SPANNABLE);
                tv_sms_recv.setVisibility(View.VISIBLE);
                //tv_sms_recv.setTextColor(Color.parseColor("#002266"));

                tv_sms_recv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OTP = GenerateRandomNumber(6);
                        Log.e("ChkOTP2",""+OTP);
                        String message2 = "Thank you for visiting Grocery Store \n Your OTP :" + OTP;
                        timerForOtp(mobileNumber,message2);
                        layout2.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);

                    }
                });

            }

        }.start();
    }

    //Timer count down
    private void updateCountDownText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%d:%d", minutes, seconds);
        //text_otp_expire.setText(timeLeftFormatted);
        Spannable WordtoSpan = new SpannableString("OTP expire after "+timeLeftFormatted+"  ");
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 17, 21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_otp_expire.setText(WordtoSpan);

    }

    void sendOTP(String mobileNumber, String message) {
        OTPServices.getInstance(AuthUserActivity.this).SendOTP(mobileNumber, message, new ApiStatusCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AuthUserActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(AuthUserActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnknownError(Exception e) {
                Toast.makeText(AuthUserActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Generate Random Number for OTP
    String GenerateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }


    //Back button
    @Override
    public void onBackPressed() {

        if (prefManager.getAUTH_BACK()==1) {
            Log.e("currentStep0"," "+" "+prefManager.getAUTH_BACK());
            Utility.launchActivity(AuthUserActivity.this, SelectRoleActivity.class, true);

        }else if (prefManager.getAUTH_BACK()==2) {
            //mCountDownTimer.cancel();
            //step_view.go(0, true);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            tv_verify.setText("Verify");
            tv_verify.setVisibility(View.VISIBLE);
            prefManager.setAUTH_BACK(1);
            Log.e("currentStep1"," "+" "+prefManager.getAUTH_BACK());

        } else if (prefManager.getAUTH_BACK() == 3 ) {
            //step_view.go(1, true);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);
            tv_verify.setVisibility(View.GONE);
            prefManager.setAUTH_BACK(2);
            Log.e("currentStep2"," "+" "+prefManager.getAUTH_BACK());
        }
    }



}


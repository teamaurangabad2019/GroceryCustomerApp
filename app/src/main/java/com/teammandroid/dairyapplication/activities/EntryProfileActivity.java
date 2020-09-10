package com.teammandroid.dairyapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.Network.AuthServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.model.DeliveryboyStatusModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EntryProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EntryProfileActivity.class.getSimpleName();

    EditText  et_address;
    Button btn_save;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    SessionHelper sessionHelper;


    private int PICK_IMAGE_REQUEST = 1;
    Activity activity;

    private ImageView iv_backprofile;
    private ImageView iv_male_avatar;
    private ImageView iv_edit;

    private Button btn_submit;
    /**
     *
     */
    private EditText et_fullname;
    /**
     *
     */
    private EditText et_mobileno;
    /**
     *
     */
    private EditText et_email;
    String mobileNo;
    String fullname;
    String email, address;
    ProgressDialog pbLoading;

    File fileToUpdate = null;
    private Bitmap bitmap;
    private Uri filePath;

    private UserModel userModel;

    public static final int RequestPermissionCode = 2;
        FloatingActionButton fb_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
        btnListeners();
        EnableRuntimePermission();

        iv_backprofile.setVisibility(View.GONE);
        fb_create.setVisibility(View.GONE);

        Log.e("TAG","prefUserid "+prefManager.getUSER_ID());

        GetUser(prefManager.getUSER_ID());

    }

    private void bindViews() {
        iv_backprofile = (ImageView) findViewById(R.id.iv_backprofile);
        iv_male_avatar = (ImageView) findViewById(R.id.iv_male_avatar);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_fullname = (EditText) findViewById(R.id.et_fullname);
        fb_create =  findViewById(R.id.fb_create);
        et_mobileno =  findViewById(R.id.et_mobileno);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);

        prefManager = new PrefManager(EntryProfileActivity.this);
        progressDialog = new ProgressDialog(EntryProfileActivity.this);
        sessionHelper = new SessionHelper(EntryProfileActivity.this);
        pbLoading = new ProgressDialog(EntryProfileActivity.this);
        //  e1 = findViewById(R.id.tv_fname);
    }

    void btnListeners() {

        btn_submit.setOnClickListener(this);

        iv_backprofile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_backprofile:

                break;

            case R.id.iv_edit:
                showFileChooser();
                break;
            case R.id.btn_submit:
                email    = et_email.getText().toString();
                fullname = et_fullname.getText().toString();
                mobileNo = et_mobileno.getText().toString();
                address    = et_address.getText().toString();

                if ( et_fullname.getText().toString().equals("") ||
                        et_mobileno.getText().toString().equals("") ||
                        et_email.getText().toString().equals("") ||
                        et_address.getText().toString().equals("") )
                {
                    Utility.showErrorMessage(EntryProfileActivity.this,"Please enter all the details !!");
                }
                else if(mobileNo.length() < 10)
                {
                    et_mobileno.setError("Enter a Phone Number");
                    et_mobileno.requestFocus();
                }
                else if (!Utility.emailValidate(email))
                {
                    et_email.setError("Enter a valid email");
                }
                else
                {

                    updateUserProfile(fullname, et_address.getText().toString(), mobileNo, email);

                    //updateUserProfile(fullname,address,mobileNo,email);
                    //updateDialog();
                    //Utility.launchActivity(EntryProfileActivity.this,HomepageActivity.class,true);
                }
                break;
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        // cursor.moveToFirst();
        if (cursor !=null)
        {
            cursor.moveToFirst();
        }
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void showFileChooser() {
        // Toast.makeText(getApplicationContext(), "press", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void GetUser(int UserId) {
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
                                Log.e("UserModel "," "+response.get(0).getUserid());
                                userModel = response.get(0);
                                et_fullname.setText(response.get(0).getFullname());
                                et_address.setText(response.get(0).getAddress());
                                et_mobileno.setText(response.get(0).getMobile());
                                et_email.setText(response.get(0).getEmail());

                                String profile = Constants.URL_USER_PROFILE_PICTURE + userModel.getProfilepic();
                                Picasso.with(EntryProfileActivity.this).load(profile).placeholder(R.drawable.male_avatar).into(iv_male_avatar);

                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                Utility.showErrorMessage(EntryProfileActivity.this, "Check your network Connection !");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Utility.showErrorMessage(EntryProfileActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(EntryProfileActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(EntryProfileActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(EntryProfileActivity.this, ex.getMessage());
        }
    }

    private void updateUserProfile(String fullname,
                                    String address,String mobileNo, String email) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.e("createParent: ", "fullname " + fullname + " " + mobileNo +" "+email+" "+address);

                if (filePath != null) {
                    Log.e("createParent: ", "notnull");
                    File path = new File(getPath(filePath));
                    String pathdd = String.valueOf(path);
                    Log.e(TAG, "uploadWithFilePath: " + pathdd);
                    //Creating a multi part request

                    AndroidNetworking.upload(Constants.URL_USER)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Userid", String.valueOf(userModel.getUserid()))
                            .addMultipartParameter("Fullname",fullname)
                            .addMultipartParameter("Roleid", String.valueOf(userModel.getRoleid()))
                            .addMultipartParameter("Address", address)
                            .addMultipartParameter("Mobile", mobileNo)
                            .addMultipartParameter("Token", "")
                            .addMultipartParameter("Email", email)
                            .addMultipartFile("Profilepic",path)
                            .addMultipartParameter("Device", String.valueOf(userModel.getDevice()))
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
                                    progressDialog.dismiss();
                                    Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                    Utility.launchActivity(EntryProfileActivity.this, CategoryListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EntryProfileActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

                if (filePath == null) {
                    Log.e("createParentfil: ", "null" );

                    // File path = new File(getPath(filePath));
                    //String pathdd=String.valueOf(path);
                    //Log.e(TAG, "uploadWithFilePath: " + pathdd );


                    AndroidNetworking.upload(Constants.URL_USER)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Userid", String.valueOf(userModel.getUserid()))
                            .addMultipartParameter("Fullname",fullname)
                            .addMultipartParameter("Roleid", String.valueOf(userModel.getRoleid()))
                            .addMultipartParameter("Address", address)
                            .addMultipartParameter("Mobile", mobileNo)
                            .addMultipartParameter("Token", "")
                            .addMultipartParameter("Email", email)
                            .addMultipartParameter("Profilepic",userModel.getProfilepic())
                            .addMultipartParameter("Device", String.valueOf(userModel.getDevice()))
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
                                    progressDialog.dismiss();
                                    Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                    Utility.launchActivity(EntryProfileActivity.this, CategoryListActivity.class,true);
                                    //updateDialog();
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponse: Profile " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseProfile " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EntryProfileActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

            } else {
                Utility.showErrorMessage(EntryProfileActivity.this, "Could not connect to the internet");
            }

        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(EntryProfileActivity.this, ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                fileToUpdate = new File(filePath.getPath());
                String filepath = fileToUpdate.getAbsolutePath();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_male_avatar.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("path", "" + filePath);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EntryProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Toast.makeText(activity, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EntryProfileActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }




}

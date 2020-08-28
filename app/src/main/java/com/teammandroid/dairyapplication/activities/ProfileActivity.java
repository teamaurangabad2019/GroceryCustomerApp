package com.teammandroid.dairyapplication.activities;

import android.Manifest;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    ImageView img_openDrawer, img_edit;
    DrawerLayout Drawer_Layout;
    EditText tv_fname, et_username, et_contact, et_address, tv_couponcode, tv_elocation, et_password;
    Button btn_save;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    SessionHelper sessionHelper;

    TextView nameheader;
    private static final int REQUEST_CHOOSER = 1234;


    private int PICK_IMAGE_REQUEST = 1;
    Activity activity;
    private Bitmap currentImage;
    private ImageView iv_backprofile;
    private ImageView iv_male_avatar;
    private ImageView iv_edit;

    private TextView tv_state;
    /**
     * Aurangabad
     */
    private TextView tv_city;
    /**
     *
     */

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
        btnListeners();
        EnableRuntimePermission();
        iv_backprofile.setVisibility(View.GONE);
        userModel=getIntent().getParcelableExtra("userData");
        mobileNo=getIntent().getStringExtra("number");
        Log.e("ProfileNumber"," "+mobileNo +" "+userModel.getUserid());

        et_mobileno.setText(mobileNo);
    }

    private void bindViews() {
        iv_backprofile = (ImageView) findViewById(R.id.iv_backprofile);
        iv_male_avatar = (ImageView) findViewById(R.id.iv_male_avatar);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_city = (TextView) findViewById(R.id.tv_city);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_fullname = (EditText) findViewById(R.id.et_fullname);
        et_mobileno =  findViewById(R.id.et_mobileno);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);

        prefManager = new PrefManager(ProfileActivity.this);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        sessionHelper = new SessionHelper(ProfileActivity.this);
        pbLoading = new ProgressDialog(ProfileActivity.this);
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
                    Utility.showErrorMessage(ProfileActivity.this,"Please enter all the details !!");
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
                    if (fileToUpdate == null) {
                        Utility.showErrorMessage(ProfileActivity.this,"Please select profile Photo !!");
                    } else {
                        UpdateUserWithPic();
                    }
                    //Utility.launchActivity(ProfileActivity.this,HomepageActivity.class,true);
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

    private void UpdateUserWithPic() {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                /*lyt_progress_reg.setVisibility(View.VISIBLE);
                lyt_progress_reg.setAlpha(1.0f);*/
                pbLoading.setTitle("Please Wait...");
                pbLoading.setCancelable(false);
                pbLoading.show();

                if (filePath != null) {
                    File path = new File(getPath(filePath));
                    Log.e(TAG, "uploadWithFilePath: " + path);
                    prefManager.setProfilePath(String.valueOf(path));

                    //Creating a multi part request
                    AndroidNetworking.upload(Constants.URL_LOGIN)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Userid", String.valueOf(userModel.getUserid()))
                            .addMultipartParameter("Fullname", fullname)
                            .addMultipartParameter("Roleid", "2")
                            .addMultipartParameter("Address",address)
                            .addMultipartParameter("Mobile", mobileNo)
                            .addMultipartParameter("Token", "")
                            .addMultipartParameter("Email", email)
                            .addMultipartFile("Profilepic",path)
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
                                    Utility.launchActivity(ProfileActivity.this,HomepageActivity.class,true);
                                    //Utility.launchActivity(AuthenticationActivity.this, ProfileActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseAuth " + response1.getMessage());

                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseUser " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(ProfileActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }
            } else {
                Utility.showErrorMessage(activity, "Could not connect to the internet");
            }

        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            pbLoading.dismiss();
            Utility.showErrorMessage(activity, ex.getMessage());
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Toast.makeText(activity, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

}

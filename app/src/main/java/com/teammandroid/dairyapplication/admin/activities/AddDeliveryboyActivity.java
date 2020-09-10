package com.teammandroid.dairyapplication.admin.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.teammandroid.dairyapplication.Network.UserServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDeliveryboyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddDeliveryboyActivity.class.getSimpleName() ;

    private CircleImageView iv_pic;
    private ImageView iv_pedit;
    /**
     * Username
     */
    private EditText et_name;
    /**
     * contact
     */
    private EditText et_mobile;
    /**
     * Email
     */
    private EditText et_email;
    /**
     * location
     */
    private EditText et_address;
    /**
     * Submit
     */
    private Button btn_submit;
    ProgressDialog progressDialog;

    private Uri filePath;
    File fileToUpdate = null;
    private Bitmap bitmap;

    String path;
    private int PICK_IMAGE_REQUEST = 1;

    String fullname;
    String mobileNo;
    String email, address;
    ImageView iv_backprofile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deliveryboy);

        initView();
    }

    private void initView() {
        iv_pic = (CircleImageView) findViewById(R.id.iv_pic);
        iv_pedit = (ImageView) findViewById(R.id.iv_pedit);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        iv_backprofile =  findViewById(R.id.iv_backprofile);
        iv_backprofile.setOnClickListener(this);
        iv_pedit.setOnClickListener(this);
        progressDialog = new ProgressDialog(AddDeliveryboyActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_submit:

                email = et_email.getText().toString();
                fullname = et_name.getText().toString();
                mobileNo = et_mobile.getText().toString();
                address = et_address.getText().toString();

                if (et_name.getText().toString().equals("")
                        || et_email.getText().toString().equals("")
                        || et_mobile.getText().toString().equals("")
                        || (et_address.getText().toString().equals("")) ) {
                    Toast.makeText(AddDeliveryboyActivity.this, "Enter all details",Toast.LENGTH_SHORT).show();

                } else if (mobileNo.length() < 10) {
                    et_mobile.setError("Enter a Phone Number");
                    et_mobile.requestFocus();
                } else if (!Utility.emailValidate(email)) {
                    et_email.setError("Enter a valid email");
                } else {

                    Log.d(TAG, fullname +" "+address +" "+mobileNo+" "+email);

                    addDeliveryBoy(fullname, address,mobileNo, email);
                }
                break;
            case R.id.iv_pedit:
                showFileChooser();
                break;

            case R.id.iv_backprofile:
                onBackPressed();
                break;


        }
    }


    private void addDeliveryBoy(String fullname,
                                String address,String mobileNo,  String email) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.e("createParent: ", "rollNo " + fullname + " " + mobileNo);

                if (filePath != null) {
                    Log.e("createParent: ", "notnull");
                    File path = new File(getPath(filePath));
                    String pathdd = String.valueOf(path);
                    Log.e(TAG, "uploadWithFilePath: " + pathdd);
                    //Creating a multi part request

                    AndroidNetworking.upload(Constants.URL_USER)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Userid", "0")
                            .addMultipartParameter("Fullname", fullname)
                            .addMultipartParameter("Roleid","2")
                            .addMultipartParameter("Address", address)
                            .addMultipartParameter("Mobile", mobileNo)
                            .addMultipartParameter("Token", "token")
                            .addMultipartParameter("Email", email)
                            .addMultipartFile("Profilepic", path)
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
                                    Utility.launchActivity(AddDeliveryboyActivity.this, DeliveryboyListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(AddDeliveryboyActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

                if (filePath == null) {
                    Log.e("createParentfil: ", "null");

                    // File path = new File(getPath(filePath));
                    //String pathdd=String.valueOf(path);
                    //Log.e(TAG, "uploadWithFilePath: " + pathdd );


                    //Creating a multi part request
                    AndroidNetworking.upload(Constants.URL_USER)
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Userid", "0")
                            .addMultipartParameter("Fullname", fullname)
                            .addMultipartParameter("Roleid","2")
                            .addMultipartParameter("Address", address)
                            .addMultipartParameter("Mobile", mobileNo)
                            .addMultipartParameter("Token", "token")
                            .addMultipartParameter("Email", email)
                            .addMultipartFile("Profilepic", null)
                            .addMultipartParameter("Device", Utility.getDeviceName())
                            .addMultipartParameter("LogedinUserId", "0")
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
                                    Utility.launchActivity(AddDeliveryboyActivity.this, DeliveryboyListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponse: Parent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParentd " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(AddDeliveryboyActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

            } else {
                Utility.showErrorMessage(AddDeliveryboyActivity.this, "Could not connect to the internet");
            }

        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(AddDeliveryboyActivity.this, ex.getMessage());
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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
                iv_pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("path", "" + filePath);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //Get Token for notification sending
   /* private void getTokan() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Log.e(TAG,"token "+ token);
                    }
                });
    }*/

}
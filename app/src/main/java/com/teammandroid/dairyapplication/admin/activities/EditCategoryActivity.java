package com.teammandroid.dairyapplication.admin.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EditCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditCategoryActivity.class.getSimpleName();
    Activity activity;
    ProgressDialog progressDialog;

    private ImageView iv_backprofile;
    /**
     * Topic Name
     */
    private EditText et_titlename;
    /**
     * Attachment
     */
    private TextView et_attach;
    /**
     * Your Message Here
     */
    private EditText et_details;
    /**
     * Submit
     */
    private Button btn_submit;
    private int PICK_IMAGE_REQUEST = 1;
    String titlename;
    String  msg;

    CategoryModel categoryModel;

    File path;
    String fullpath;
    Uri uri;
    ArrayList<String> a;
    public static final int RequestPermissionCode = 2;

    private Uri filePath;
    File fileToUpdate = null;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        EnableRuntimePermission();
        activity = EditCategoryActivity.this;
        a=new ArrayList<>();
        initView();
        categoryModel = getIntent().getParcelableExtra("CategoryModel");

        Log.e(TAG," chk "+categoryModel.getDetails() +" "+ categoryModel.getImagename());

        et_details.setText(categoryModel.getDetails());
        et_titlename.setText(categoryModel.getTitle());
        et_attach.setText(categoryModel.getImagename());
        fullpath =categoryModel.getImagename();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_backprofile:
                onBackPressed();
                //Utility.launchActivity(AddCategoryActivity.this, HomeworkListActivity.class, true);
                break;

            case R.id.et_attach:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                Log.e(TAG, fullpath+" topicnm  " + titlename + " msg " + msg);

                break;

            case R.id.btn_submit:
                titlename = et_titlename.getText().toString();
                msg      = et_details.getText().toString();

                if (titlename.length() == 0 || msg.length() == 0) {

                    Toast.makeText(EditCategoryActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                } else {
                    // startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));
                    //Toast.makeText(AddCategoryActivity.this, "Homework Updated", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, categoryModel.getCategoryid()+" topicnm  " + titlename + " msg " + msg);

                   updateTrustProfile(categoryModel.getCategoryid(),titlename,msg);

                }
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
                //im.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("path", "" + filePath);
    }

    private void updateTrustProfile(int Categoryid,String title,String Details) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                progressDialog.setMessage("Uploading File,Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (filePath != null) {
                    Log.e("createParent: ", "notnull");
                    File path = new File(getPath(filePath));
                    String pathdd = String.valueOf(path);
                    Log.e(TAG, "uploadWithFilePath: " + path +"title "+title+" "+Details);

                    AndroidNetworking.upload(Constants.URL_CATEGORY)
                            // .addFileToUpload("", "certificate") //Adding file
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Categoryid", String.valueOf(Categoryid))
                            .addMultipartParameter("Title", title)
                            .addMultipartParameter("Details",Details)
                            .addMultipartFile("Image", path)
                            //.addMultipartParameter("Image", "path")
                            .addMultipartParameter("LogedinUserId", "1")
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
                                    Utility.launchActivity(EditCategoryActivity.this, CategoryListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditCategoryActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

                if (filePath == null) {
                    Log.e("createParentfil: ", "null");
                    //Creating a multi part request
                    AndroidNetworking.upload(Constants.URL_CATEGORY)
                            // .addFileToUpload("", "certificate") //Adding file
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "1")
                            .addMultipartParameter("Categoryid", String.valueOf(Categoryid))
                            .addMultipartParameter("Title", title)
                            .addMultipartParameter("Details",Details)
                            //.addMultipartFile("Image", null)
                            .addMultipartParameter("Image", categoryModel.getImagename())
                            .addMultipartParameter("LogedinUserId", "1")
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
                                    Utility.launchActivity(EditCategoryActivity.this, CategoryListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.toString(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponse: Parent " + response1.toString());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParentd " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditCategoryActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

            } else {
                Utility.showErrorMessage(EditCategoryActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(EditCategoryActivity.this, ex.getMessage());
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void initView() {
        iv_backprofile = (ImageView) findViewById(R.id.iv_backprofile);
        iv_backprofile.setOnClickListener(this);
        et_titlename = (EditText) findViewById(R.id.et_titlename);
        et_attach = (TextView) findViewById(R.id.et_attach);
        et_attach.setOnClickListener(this);
        et_details = (EditText) findViewById(R.id.et_details);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        progressDialog = new ProgressDialog(EditCategoryActivity.this);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditCategoryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(EditCategoryActivity.this, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditCategoryActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

}

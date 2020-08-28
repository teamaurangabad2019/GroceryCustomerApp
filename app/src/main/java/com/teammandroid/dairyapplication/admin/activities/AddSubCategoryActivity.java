package com.teammandroid.dairyapplication.admin.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.UriUtils;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AddSubCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddSubCategoryActivity.class.getSimpleName();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subcategory);

        EnableRuntimePermission();
        activity = AddSubCategoryActivity.this;
        a=new ArrayList<>();

        categoryModel = getIntent().getParcelableExtra("CategoryModel");

        Log.e(TAG," chk "+categoryModel.getCategoryid() +" "+ categoryModel.getImagename());

        initView();

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
                //Log.e(TAG, fullpath+" topicnm  " + titlename + " msg " + msg);

                break;

            case R.id.btn_submit:
                titlename = et_titlename.getText().toString();
                msg      = et_details.getText().toString();

                if (titlename.length() == 0 || msg.length() == 0) {

                    Toast.makeText(AddSubCategoryActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                } else {
                    // startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));
                    //Toast.makeText(AddCategoryActivity.this, "Homework Updated", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, categoryModel.getCategoryid()+" path "+fullpath+" topicnm  " + titlename + " msg " + msg);

                   AddMessageWithFile(categoryModel.getCategoryid(),fullpath,titlename,msg);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //1 img //3 audio //4 video //5 document

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {

               if (data.getData() != null)
               {
                    uri = data.getData();
                    Log.e(TAG,"uri "+uri);

                    //fullpath= getPath(uri);
                    fullpath = UriUtils.getPathFromUri(this,uri);

                    Log.e(TAG,"fullpath "+fullpath);
                    if(fullpath.endsWith("jpg") || fullpath.endsWith("jpeg")  || fullpath.endsWith("png"))
                    {
                        //Toast.makeText(AddCategoryActivity.this, "correct", Toast.LENGTH_SHORT).show();
                        a.add(fullpath);
                    }
                    else {
                        Toast.makeText(AddSubCategoryActivity.this, "Select only images", Toast.LENGTH_SHORT).show();
                    }

                    //a.add(uri);
                }

                et_attach.setText("You selected total "+ 1 +" Images");

                /*if (a.size()==0)
                {
                    et_attach.setText("You selected total "+ 1 +" Images");
                    Log.e(TAG,"size "+(a.size()+1));
                }
                else if (a.size()>0)
                {
                    et_attach.setText("You selected total "+ 1 +" Images");
                    Toast.makeText(AddCategoryActivity.this, "You can Pick only single image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"size elseif "+(a.size()));
                }else
                {
                    Toast.makeText(AddCategoryActivity.this, "You can Pick only single image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"else "+(a.size()));
                }*/
            }
        }
        Log.e( "onActivityResult:2 ", a.toString());
    }


    private void AddMessageWithFile(int Categoryid,String imgpath, String title,String Details) {
        try {
            //  uriData = media.getUri();
            path = new File(imgpath);
            Log.e(TAG, "uploadWithFilePath: " + path +"title "+title+" "+Details);

            //{"type":3,"Action":1,"Attachmentid":0,"Homeworkid":3,"Typeid":1,"Filename":"Filename","LogedinUserId":1}
            int UserId = 1;
            AndroidNetworking.upload(Constants.URL_SUB_CATEGORY)
                    // .addFileToUpload("", "certificate") //Adding file
                    .addMultipartParameter("type", "1")
                    .addMultipartParameter("Action", "1")
                    .addMultipartParameter("Subcategoryid", "0")
                    .addMultipartParameter("Categoryid", String.valueOf(Categoryid))
                    .addMultipartParameter("Title", title)
                    .addMultipartParameter("Details",Details)
                    .addMultipartFile("Image", path)
                    //.addMultipartParameter("Imagename", "path")
                    .addMultipartParameter("LogedinUserId", "1")
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            Log.e(TAG, "uploadImage: totalBytes: " + totalBytes);
                            Log.e(TAG, "uploadImage: bytesUploaded: " + bytesUploaded);
                            progressDialog.setMessage("Attaching File, Please wait...");
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.hide();
                            //dialog.dismiss();
                            //GetMsgDetails(Integer.parseInt(response.getString("Result")), media);
                            Log.e(TAG, "FileonRes: " + response.toString());
                            Toast.makeText(activity, response.toString() +" Successful!!", Toast.LENGTH_SHORT).show();
                            Utility.launchActivity(AddSubCategoryActivity.this, CategoryListActivity.class, true);
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "CategoryError: ", error);
                            progressDialog.hide();
                        }
                    });
        } catch (Exception exc) {
            Log.e(TAG, "InsertMessageWithPdf: " + exc.getMessage());
            Toast.makeText(activity, exc.getMessage(), Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(AddSubCategoryActivity.this);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddSubCategoryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(AddSubCategoryActivity.this, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(AddSubCategoryActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }



}

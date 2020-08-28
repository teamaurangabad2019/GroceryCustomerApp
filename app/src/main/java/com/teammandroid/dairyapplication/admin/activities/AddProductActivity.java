package com.teammandroid.dairyapplication.admin.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.admin.model.SubcategoryModel;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.UriUtils;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddProductActivity.class.getSimpleName();
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
     private EditText et_Ourprice;
     private EditText et_Offer;
    /**
     * Attachment
     */
    private TextView et_attach;
    private TextView et_attach2;
    /**
     * Your Message Here
     */
    private EditText et_details;
    /**
     * Submit
     */
    private EditText et_price;
    /**
     * Submit
     */
    private Button btn_submit;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_MULTIPLE_IMAGE_REQUEST = 2;
    String titlename,titlename2;
    String  msg;
    double actualPrice,ourPrice;
    int offer;

    CategoryModel categoryModel;

    File path,path1;
    String fullpath,fullpath2;
    Uri uri;
    Uri uri2;
    ArrayList<String> a;
    ArrayList<String> a1;
    public static final int RequestPermissionCode = 2;
    String numberToPass = "1" ;//default 1 for male
    RadioGroup rg ;
    RadioButton radioButton_avalable ;
    RadioButton radioButton_notavalable;
    SubcategoryModel subcategoryModel;

    double amount;
    double result=0.0;
    String totalfees;
    double discountFees;

    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        EnableRuntimePermission();
        activity = AddProductActivity.this;

        a = new ArrayList<>();
        a1 = new ArrayList<>();

        initView();

        et_Ourprice.setEnabled(false);

        subcategoryModel = getIntent().getParcelableExtra("SubcategoryModel");

         Log.e(TAG,"subId "+subcategoryModel.getSubcategoryid());
         rg = (RadioGroup) findViewById(R.id.group);
         radioButton_avalable = (RadioButton) findViewById(R.id.radioButton_avalable);
         radioButton_notavalable = (RadioButton) findViewById(R.id.radioButton_notavalable);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, final int count) {
                Log.e("modelAddDaily","textchanged");

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    et_Ourprice.setText("0");

                    calculateTotalFeeChanged();
                } catch (Exception e) {
                    //  tv_totalamt.setText(amount);
                    e.printStackTrace();
                }
            }
        };
        et_price.addTextChangedListener(textWatcher);

        final TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, final int count) {
                Log.e("modelAddDaily","textchanged");

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    et_Ourprice.setText("0");

                    calculateDiscountChanged();
                } catch (Exception e) {
                    //  tv_totalamt.setText(amount);
                    e.printStackTrace();
                }
            }
        };
        et_Offer.addTextChangedListener(textWatcher1);
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

                break;

                case R.id.et_attach2:

                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent2, PICK_MULTIPLE_IMAGE_REQUEST);

                //insertMultipleImages();
                break;

            case R.id.btn_submit:

                /*
                for(int i=0 ; i<= a1.size(); i++)
                {
                    // Toast.makeText(AddProductActivity.this, "chk "+a.get(i).toString(), Toast.LENGTH_SHORT).show();
                    if (a1.size() != 0) {
                        Log.e(TAG, "chk " + a1.get(i));
                    }else
                    {
                        Log.e(TAG, "chk " + "dvvvvvvv");

                    }
                   // AddMultipleFiles(a1.get(i).toString(), 22);
                }*/

                 titlename = et_titlename.getText().toString();
                  msg      = et_details.getText().toString();
                  actualPrice = Double.parseDouble(et_price.getText().toString());
                  offer      = Integer.parseInt(et_Offer.getText().toString());
                  ourPrice   = Double.parseDouble(et_Ourprice.getText().toString());

                if (titlename.length() == 0 || msg.length() == 0) {

                    Toast.makeText(AddProductActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "path "+fullpath+" topicnm  " + titlename + " msg " + msg);

                    if(radioButton_avalable.isChecked())
                    {
                        Toast.makeText(activity, "111", Toast.LENGTH_SHORT).show();

                        AddMessageWithFile(0,titlename,msg,actualPrice,ourPrice,offer,1,
                                subcategoryModel.getSubcategoryid(),fullpath);
                    }
                    else  if(radioButton_notavalable.isChecked())
                    {
                        Toast.makeText(activity, "222", Toast.LENGTH_SHORT).show();
                        AddMessageWithFile(0,titlename,msg,actualPrice,ourPrice,offer,0,
                                subcategoryModel.getSubcategoryid(),fullpath);
                    }



                }
              break;
        }
    }

    private void insertMultipleImages() {

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
                        Toast.makeText(AddProductActivity.this, "Select only images", Toast.LENGTH_SHORT).show();
                    }

                    //a.add(uri);
                }
                et_attach.setText("You selected total "+  1 +" Images");

            }

            else if (requestCode == PICK_MULTIPLE_IMAGE_REQUEST) {

                if (data.getData() != null)
               {
                   Uri uri2 = data.getData() ;
                    Log.e(TAG,"uri2 "+uri2);

                    //fullpath= getPath(uri2);
                    String fullpath2 = UriUtils.getPathFromUri(this,uri2);

                    Log.e(TAG,"fullpath2 "+fullpath2);
                    if(fullpath2.endsWith("jpg") || fullpath2.endsWith("jpeg")  || fullpath2.endsWith("png"))
                    {
                        //Toast.makeText(AddCategoryActivity.this, "correct", Toast.LENGTH_SHORT).show();
                        a1.add(fullpath2);
                    }
                    else {
                        Toast.makeText(AddProductActivity.this, "Select only images", Toast.LENGTH_SHORT).show();
                    }
                    //a.add(uri2);
                }
                et_attach2.setText("You selected total "+ a1.size() +" Images");

            }
        }
        Log.e( "onActivityResult:2 ", a.toString() +" "+ a1.toString());
    }

    private void AddMessageWithFile(int Productid, String title,String Details,double Price,double Ourprice,
                                    int Offer,int Isavailable,int Subcategory,String imgpath) {
        try {
            path = new File(imgpath);
            Log.e(TAG, "uploadWithFilePath: " + path +"title "+title+" "+Details);

            //{"type":1,"Action":1,"Productid":0, "Title":"88","Details":"Details","Price":1,
            // "Ourprice":1,"Offer":1,"Isavailable":1,"Subcategory":1,"Image":"Imagename", "LogedinUserId":1}
            int UserId = 1;
            AndroidNetworking.upload(Constants.URL_Product)
                    // .addFileToUpload("", "certificate") //Adding file
                    .addMultipartParameter("type", "1")
                    .addMultipartParameter("Action", "1")
                    .addMultipartParameter("Productid", String.valueOf(Productid))
                    .addMultipartParameter("Title", title)
                    .addMultipartParameter("Details",Details)
                    .addMultipartParameter("Price", String.valueOf(Price))
                    .addMultipartParameter("Ourprice", String.valueOf(Ourprice))
                    .addMultipartParameter("Offer", String.valueOf(Offer))
                    .addMultipartParameter("Isavailable", String.valueOf(Isavailable))
                    .addMultipartParameter("Subcategory", String.valueOf(Subcategory))
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

                            TypeToken<Response> token = new TypeToken<Response>() {
                            };
                            progressDialog.dismiss();
                            Response response1 = new Gson().fromJson(response.toString(), token.getType());
                             Log.e(TAG, "Res " + response1.getResult()+"msg "+response1.getMessage());

                            Toast.makeText(activity, response.toString() +" Successful!!", Toast.LENGTH_SHORT).show();

                            /*for(int i=0 ; i<= a1.size(); i++)
                            {
                               // Toast.makeText(AddProductActivity.this, "chk "+a.get(i).toString(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "chk "+ a1.get(i).toString());
                                AddMultipleFiles(a1.get(i).toString(), response1.getResult());
                            }*/
//                            Utility.launchActivity(AddProductActivity.this, ProductListActivity.class, true);

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

    private void AddMultipleFiles(String imgpath , int homeworkId) {
        Log.e(TAG, "uploadWithFilePath1: " + imgpath  +" "+path1);

        try {
            //  uriData = media.getUri();
            path1 = new File(imgpath);
            Log.e(TAG, "uploadWithFilePath11: " + path1.toString()+homeworkId);

            //{"type":3,"Action":1,"Attachmentid":0,"Homeworkid":3,"Typeid":1,"Filename":"Filename","LogedinUserId":1}
            int UserId = 1;
            AndroidNetworking.upload(Constants.URL_Product_MULTIPLE_IMG)
                    // .addFileToUpload("", "certificate") //Adding file
                    .addMultipartParameter("type", "1")
                    .addMultipartParameter("Action", "1")
                    .addMultipartParameter("Imageid", "0")
                    .addMultipartParameter("Productid", String.valueOf(homeworkId))
                    //.addMultipartFile("Image", path1)
                    .addMultipartParameter("Image", "ssss")
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
                            //Toast.makeText(activity, response.getString("Message"), Toast.LENGTH_SHORT).show();

                            Utility.launchActivity(AddProductActivity.this, ProductListActivity.class, true);

                        }

                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "FileonError: ", error);
                            progressDialog.hide();
                        }
                    });
        }
        catch (Exception exc) {
            Log.e(TAG, "InsertMessageWithPdfExc: " + exc.getMessage());
           // Toast.makeText(activity, exc.getMessage(), Toast.LENGTH_SHORT).show();
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
        et_Offer = (EditText) findViewById(R.id.et_Offer);
        et_price = (EditText) findViewById(R.id.et_price);
        et_Ourprice = (EditText) findViewById(R.id.et_Ourprice);
        et_attach = (TextView) findViewById(R.id.et_attach);
        et_attach2 = (TextView) findViewById(R.id.et_attach2);
        et_attach.setOnClickListener(this);
        et_attach2.setOnClickListener(this);
        et_details = (EditText) findViewById(R.id.et_details);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        progressDialog = new ProgressDialog(AddProductActivity.this);
        dbHelper = new DatabaseHelper(AddProductActivity.this);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(AddProductActivity.this, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

    private void calculateTotalFeeChanged() throws NumberFormatException
    {
        double dis=0.0;
        double s=0.0;

        Editable markedprice= (Editable) et_price.getText();
        dis= Double.parseDouble(et_Offer.getText().toString());

        Log.e("amt",""+dis);

        double value1 = 0.0;

        if (markedprice != null)
            value1 = Double.parseDouble(markedprice.toString());


        Log.e("value1",""+value1);

        Log.e("value2",""+dis);
        s = 100 - dis;

        result = (s*value1)/100;;
        et_Ourprice.setText(String.valueOf(result));
        //totalfees= Double.parseDouble(et_price.getText().toString());
        Log.e("totalfees ",""+totalfees);
    }

    private void calculateDiscountChanged() throws NumberFormatException
    {
        double dis=0.0;
        double s=0.0;

        Editable markedprice= (Editable) et_Offer.getText();
        dis= Double.parseDouble(et_price.getText().toString());

        Log.e("amt",""+dis);

        double value1 = 0.0;

        if (markedprice != null)
            value1 = Double.parseDouble(markedprice.toString());


        Log.e("value1",""+value1);

        Log.e("value2",""+dis);
        // s = 100 - dis;

        result = (value1/100);//1000-100
        s = result * dis; //0.1*10
        amount=dis-s; //1000-100

        //Toast.makeText(getApplicationContext(),""+s+" "+amount,Toast.LENGTH_LONG).show();// STOPSHIP: 5/6/2020
        Log.e("discountFees "," "+discountFees+" "+amount);

        et_Ourprice.setText(String.valueOf(amount));
        discountFees=Double.parseDouble(String.valueOf(amount));
        Log.e("discountFees ",""+discountFees);
    }




}

package com.teammandroid.dairyapplication.admin.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditDeliveryBoyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditDeliveryBoyActivity.class.getSimpleName();
    private Uri filePath;
    String path;
    private int PICK_IMAGE_REQUEST = 1;
    public static final int RequestPermissionCode = 2;

    UserModel model;


    File fileToUpdate = null;
    private Bitmap bitmap;

    String fullname;
    String mobileNo;
    String email, address, address1;


    ProgressDialog progressDialog;
    Activity activity;


    String selectedDate;
    Calendar cal;
    String date = "";

    LinearLayout ll_classselection;
    RadioGroup radioGroup;
    int r1 = 0;
    PrefManager prefManager;
    TextView tv_date;
    Dialog resultbox;
    String dateCollected;
    private ImageView iv_backprofile;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deliveryboy);
        initView();

        EnableRuntimePermission();


        model = getIntent().getParcelableExtra("userModel");
        bindValues();

        cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        SimpleDateFormat Real_formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date Show_date = new Date();
        //tv_date.setText(Real_formatter.format(Show_date));


    }

    private void bindValues() {

        String profile = Constants.URL_USER_PROFILE_PICTURE + model.getProfilepic();
        // String profile = model.getProfilepic();
        Log.e(TAG, "bindValues: profile " + profile);
        Picasso.with(this).load(profile).placeholder(R.drawable.male_avatar).into(iv_pic);

        Log.e("fullname", "id " + model.getUserid() + " fullname " + model.getFullname() + "mob " + model.getMobile());
        et_name.setText(model.getFullname());
        et_mobile.setText(model.getMobile());
        et_email.setText(model.getEmail());
        et_address.setText(model.getAddress());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backprofile:
                Utility.launchActivity(EditDeliveryBoyActivity.this, DeliveryboyListActivity.class,
                        true);

                break;

            case R.id.btn_submit:
                email = et_email.getText().toString();
                fullname = et_name.getText().toString();
                mobileNo = et_mobile.getText().toString();
                address = et_address.getText().toString();

                if (et_name.getText().toString().equals("")
                        || et_mobile.getText().toString().equals("")
                        || (et_email.getText().toString().equals(""))
                        || (et_address.getText().toString().equals(""))) {

                    Toast.makeText(EditDeliveryBoyActivity.this,"PLease enter all details!!",Toast.LENGTH_SHORT).show();
                } else if (mobileNo.length() < 10) {
                    et_mobile.setError("Enter a Phone Number");
                    et_mobile.requestFocus();
                } else if (!Utility.emailValidate(email)) {
                    et_email.setError("Enter a valid email");
                } else {

                    addDeliveryBoy(model.getUserid(),fullname, address,mobileNo, email);

                    Log.e(TAG, "class " + fullname + " " + mobileNo + " " + email + " " + address + " " );
                    // Toast.makeText(CreateTeachingStaffActivity.this," 1",Toast.LENGTH_LONG).show();



                }

                break;
            case R.id.iv_pedit:
                showFileChooser();
                break;

            case R.id.rl_date:
                // Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(this,
                        R.style.AppBlackTheme,
                        datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                break;
        }
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

            //2019-11-03
            date = year1 + "-" + month1 + "-" + day1;
            Log.e(TAG, date);
            //view.setMaxDate(Long.parseLong(date));

            int m = Integer.parseInt(month1);
            // tv_date.setText(day1 + "/" + month1 + "/" + year1);
            //selectedDate = date;

            String month = null;
            if (m == 1) {
                month = "January";
            } else if (m == 2) {
                month = "February";
            } else if (m == 3) {
                month = "March";
            } else if (m == 4) {
                month = "April";
            } else if (m == 5) {
                month = "May";
            } else if (m == 6) {
                month = "June";
            } else if (m == 7) {
                month = "July";
            } else if (m == 8) {
                month = "August";
            } else if (m == 9) {
                month = "September";
            } else if (m == 10) {
                month = "October";
            } else if (m == 11) {
                month = "November";
            } else if (m == 12) {
                month = "December";
            }

            selectedDate = day1 + "-" + month + "-" + year1;
            Log.e(TAG, "date " + selectedDate);
            tv_date.setText(day1 + "-" + month + "-" + year1);
            dateCollected = selectedDate;//tv_date.getText().toString();

        }

    };

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
        if (cursor != null) {
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

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditDeliveryBoyActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(activity, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditDeliveryBoyActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

    private void showCustomDialogNoRecord() {
        // this.correct = correct;
        resultbox = new Dialog(EditDeliveryBoyActivity.this);
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
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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
        iv_backprofile = (ImageView) findViewById(R.id.iv_backprofile);
        iv_backprofile.setOnClickListener(this);
        iv_pic = (CircleImageView) findViewById(R.id.iv_pic);
        iv_pedit = (ImageView) findViewById(R.id.iv_pedit);
        iv_pedit.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        progressDialog = new ProgressDialog( EditDeliveryBoyActivity.this);
    }

    private void addDeliveryBoy(int Userid,String fullname,
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
                            .addMultipartParameter("Userid", String.valueOf(Userid))
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
                                    com.google.common.reflect.TypeToken<Response> token = new com.google.common.reflect.TypeToken<Response>() {
                                    };
                                    progressDialog.dismiss();
                                    Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                    Utility.launchActivity(EditDeliveryBoyActivity.this, DeliveryboyListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditDeliveryBoyActivity.this, "Network error" + error.getMessage());
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
                            .addMultipartParameter("Userid", String.valueOf(Userid))
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
                                    com.google.common.reflect.TypeToken<Response> token = new TypeToken<Response>() {
                                    };
                                    progressDialog.dismiss();
                                    Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                    Utility.launchActivity(EditDeliveryBoyActivity.this, DeliveryboyListActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponse: Parent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParentd " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditDeliveryBoyActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

            } else {
                Utility.showErrorMessage(EditDeliveryBoyActivity.this, "Could not connect to the internet");
            }

        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(EditDeliveryBoyActivity.this, ex.getMessage());
        }
    }


}
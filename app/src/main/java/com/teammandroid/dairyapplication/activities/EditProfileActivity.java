package com.teammandroid.dairyapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.Network.AuthServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends FragmentActivity implements View.OnClickListener, LocationListener, OnMapReadyCallback {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

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

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    Button btn_pick;

    String strAdd = "";
    GoogleMap googleMap;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    Button btn_pickEdit;
    CardView cv_pick_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
        btnListeners();
        //EnableRuntimePermission();
        requestPermission();
        //cv_pick_location.setVisibility(View.GONE);
        iv_backprofile.setVisibility(View.GONE);
        fb_create.setVisibility(View.GONE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();


        Log.e("TAG","prefUserid "+prefManager.getUSER_ID());

        GetUser(prefManager.getUSER_ID());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fn_getlocation();
        }



    }

    private void fetchLocation() {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(EditProfileActivity.this);
                }
            }
        });
    }
    private void bindViews() {
        iv_backprofile = (ImageView) findViewById(R.id.iv_backprofile);
        iv_male_avatar = (ImageView) findViewById(R.id.iv_male_avatar);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        cv_pick_location =  findViewById(R.id.cv_pick_location);
        btn_pick =  findViewById(R.id.btn_pick);
        btn_pick.setOnClickListener(this);
        btn_pickEdit =  findViewById(R.id.btn_pickEdit);
        btn_pickEdit.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_fullname = (EditText) findViewById(R.id.et_fullname);
        fb_create = findViewById(R.id.fb_create);
        et_mobileno =  findViewById(R.id.et_mobileno);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);

        prefManager = new PrefManager(EditProfileActivity.this);
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        sessionHelper = new SessionHelper(EditProfileActivity.this);
        pbLoading = new ProgressDialog(EditProfileActivity.this);
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

             case R.id.btn_pickEdit:
                 if (prefManager.getSelect()==0) {
                     prefManager.setSelect(1);
                     cv_pick_location.setVisibility(View.VISIBLE);

                 }else {
                     prefManager.setSelect(0);
                     cv_pick_location.setVisibility(View.GONE);
                 }
                break;

            case R.id.btn_pick:
                Log.d(TAG,strAdd);
                Toast.makeText(this, "Cureent location11111 :" +strAdd, Toast.LENGTH_LONG).show();

                et_address.setText(strAdd);
                //updateUserProfile(fullname,strAdd,mobileNo,email);
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
                    Utility.showErrorMessage(EditProfileActivity.this,"Please enter all the details !!");
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
                    updateUserProfile(fullname,address,mobileNo,email);
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
                                Picasso.with(EditProfileActivity.this).load(profile).placeholder(R.drawable.male_avatar).into(iv_male_avatar);

                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                Utility.showErrorMessage(EditProfileActivity.this, "Check your network Connection !");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Utility.showErrorMessage(EditProfileActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(EditProfileActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(EntryProfileActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(EditProfileActivity.this, ex.getMessage());
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
                                    //onBackPressed();
                                    Utility.launchActivity(EditProfileActivity.this, HomepageActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponseParent " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseParent " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditProfileActivity.this, "Network error" + error.getMessage());
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
                                    Utility.launchActivity(EditProfileActivity.this, HomepageActivity.class,true);
                                    Toast.makeText(getApplicationContext(), "" + response1.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onResponse: Profile " + response1.getMessage());
                                }

                                @Override
                                public void onError(ANError error) {
                                    Log.e(TAG, "onResponseProfile " + error.getMessage());
                                    progressDialog.dismiss();
                                    Utility.showErrorMessage(EditProfileActivity.this, "Network error" + error.getMessage());
                                }
                            });
                }

            } else {
                Utility.showErrorMessage(EditProfileActivity.this, "Could not connect to the internet");
            }

        } catch (Exception ex) {
            // lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(EditProfileActivity.this, ex.getMessage());
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fn_getlocation() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        Log.e("latitudeGgl",location.getLatitude()+"");
                        Log.e("longitudeGgl",location.getLongitude()+"");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        getCompleteAddressString(latitude,longitude);




                        // LatLng object to store user input coordinates
                        //LatLng point = new LatLng(latitude,longitude);

                        // Drawing the marker at the coordinates
                        //drawMarker(point);

                        Toast.makeText(this, "Cureent location222 :" +latitude+"\n"+longitude , Toast.LENGTH_LONG).show();
                        //fn_update(latitude,longitude,prefManager.getUSER_ID());
                    }
                }

            }

            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        // Log.e("latitude_service",location.getLatitude()+"");
                        // Log.e("longitude_service",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //fn_update(latitude,longitude,prefManager.getUSER_ID());
                    }
                }
            }

        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {



        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("MyCurrentLocAddr", strReturnedAddress.toString());
            } else {
                Log.d("MyCurrentLocAddr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyCurrentLocAddr", "Cannot get Address!");
        }
        return strAdd;
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // startService(new Intent(CheckoutActivity.this, MyLocationService.class));

                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            // showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void drawMarker(LatLng point){
        // Clears all the existing coordinates
        googleMap.clear();

        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Setting title for the InfoWindow
        markerOptions.title("Position");

        // Setting InfoWindow contents
        markerOptions.snippet("Latitude:"+point.latitude+",Longitude"+point.longitude);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);

        // Moving CameraPosition to the user input coordinates
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Toast.makeText(activity, "Storage permission allows us to Access Storage", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
    }
}

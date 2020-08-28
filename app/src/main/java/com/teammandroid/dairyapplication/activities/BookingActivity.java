package com.teammandroid.dairyapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.SessionManager;
import com.teammandroid.dairyapplication.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //ItemsSqlite itemSqlite;

    private Spinner spinner_time;

    //ArrayList<SlotsHolder> slotsHolderArrayList;
    ArrayList<String> slotNames;

    Button btn_date, btn_Book_Now;
    public Calendar date;
    String formattedDate;
    View viewMenuIconBack;
    UserModel user;

    RelativeLayout layout_progress_appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //slotsHolderArrayList = new ArrayList<>();
        slotNames = new ArrayList<>();

        try {
            //itemSqlite = getIntent().getParcelableExtra("productModelCart");
            //Log.e("ChkItemSqlite", itemSqlite.toString());
        } catch (Exception e) {
            //Utility.showErrorMessage(this, e.getMessage());
        }

        bindView();
        btnListeneres();
        //GetSlots();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_date:
                showDateTimePicker();
                break;
            case R.id.viewMenuIconBack:
                Intent i = new Intent(getApplicationContext(), HomepageActivity.class);
                startActivity(i);
                break;
            case R.id.btn_Book_Now:
                bindingAppointment();
                break;
        }
    }

    private void bindingAppointment() {
/*
        int integer_slotid=(int)spinner_time.getSelectedItemId();

        int paction=1;
        int appointmentid=0;
        int slotid=integer_slotid+1;
        int itemid=itemSqlite.getItemid();
        int offerid=1;
        String userid=user.getMobile();
        String confirmdate=btn_date.getText().toString();
        int isconfirm=0;
        double price=itemSqlite.getTotalprice();
        int isoffer=0;
        int plogedinuserid=1;

      *//*  AppointmentsRequest appointmentsRequest = new AppointmentsRequest(paction, appointmentid, slotid, itemid, offerid, userid,
                confirmdate, isconfirm, price, isoffer, plogedinuserid);*//*

        CreateAppointments(slotid,offerid,userid,confirmdate,isconfirm,price,isoffer,plogedinuserid);
  */  }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = sdf.format(c.getTime());

                btn_date.setText(formattedDate);

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    void bindView() {
        layout_progress_appointment = findViewById(R.id.layout_progress_appointment);
        spinner_time = findViewById(R.id.spinner_time);
        btn_date = findViewById(R.id.btn_date);
        btn_Book_Now = findViewById(R.id.btn_Book_Now);
        viewMenuIconBack = findViewById(R.id.viewMenuIconBack);

        SessionManager sessionManager=new SessionManager(this);
        user= sessionManager.getUserDetails();

    }

    void btnListeneres() {
        spinner_time.setOnItemSelectedListener(this);
        btn_date.setOnClickListener(this);
        viewMenuIconBack.setOnClickListener(this);
        btn_Book_Now.setOnClickListener(this);
    }

    /*private void GetSlots() {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                SlotsServices.getInstance(getApplicationContext()).GetSlots(new ApiStatusCallBack<ArrayList<SlotsModel>>() {

                    @Override
                    public void onSuccess(ArrayList<SlotsModel> slots) {

                      //  slotsHolderArrayList = slots.getArrayList();
                        for (int i = 0; i < slots.size(); i++) {
                            SlotsModel slotsHolder = slots.get(i);
                            String starttime = slotsHolder.getStarttime();
                            String endtime = slotsHolder.getEndtime();
                            String slotfullname = starttime + "-" + endtime;
                            Log.e("CheckSlots", slotfullname);
                            slotNames.add(slotfullname);
                        }
                        Log.e("CheckReponse", slotsHolderArrayList.toString());
                        bindSlots(slotNames);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Utility.showErrorMessage(BookingActivity.this, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        Utility.showErrorMessage(BookingActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);
                    }
                });
            } else {
                Utility.showErrorMessage(this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        } catch (Exception ex) {
            Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    void bindSlots(ArrayList<String> slots) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookingActivity.this,
                android.R.layout.simple_spinner_item, slots);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter);
    }

    private void CreateAppointments(int slotid, int offerid, String userid, String confirmdate, int isconfirm, double price, int isoffer, int plogedinuserid) {


        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                layout_progress_appointment.setVisibility(View.VISIBLE);
                layout_progress_appointment.setAlpha(1.0f);

                AppointmentServices.getInstance(getApplicationContext()).CreateAppointment(slotid,offerid,userid,confirmdate,isconfirm,price,isoffer,plogedinuserid, new ApiStatusCallBack<Response>() {

                    @Override
                    public void onSuccess(Response response) {
                        if (response.getHasError() == 0) {
                            layout_progress_appointment.setVisibility(View.GONE);

                            Utility.showErrorMessage(BookingActivity.this, response.getMessage(), Snackbar.LENGTH_LONG);

                          *//*  AppUserSqliteOperations appUserSqliteOperations=new AppUserSqliteOperations(BookingActivity.this);
                            AppUser appUser=appUserSqliteOperations.GetAppUser();*//*
                            String userid=user.getMobile();
                            String mobilenumber="91"+userid;
                            String admin_message=userid +" booked an appointment for "+itemSqlite.getItemname();
                            String client_message= "Thank you for Booking appointment for "+itemSqlite.getItemname()+" Please wait for confirmation "+" - GLORIOUS DIVAS";
                            sendMessage("919860286108",admin_message);
                            sendMessage(mobilenumber,client_message);
                            String notification_title="Thank You";
                            String notification_body="Booking appointment for "+itemSqlite.getItemname()+" Please wait for confirmation ";

                            FirebaseNotificationServices.getInstance(BookingActivity.this).CreateNotification(notification_title,notification_body);

                            Notification notification=new Notification();
                            notification.setMessageid(0);
                            notification.setMessage(notification_body);
                            notification.setMsgtime(Utility.getCurrentDateTime());
                            notification.setIsread(0);
                            notification.setType(1);
                            saveNotificationToSqlite(notification);

                            BookingSqliteOperations bookingSqliteOperations = new BookingSqliteOperations(BookingActivity.this);
                            long responce = bookingSqliteOperations.deleteBookings(itemSqlite.getBookingid());
                            if (responce>0){
                                Utility.showErrorMessage(BookingActivity.this,"removed", Snackbar.LENGTH_LONG);
                                Utility.launchActivity(BookingActivity.this,HomeActivity.class,true);

                            }else {
                                Utility.showErrorMessage(BookingActivity.this,"SomeThing went Wrong ", Snackbar.LENGTH_LONG);
                            }

                        } else {
                            Utility.showErrorMessage(BookingActivity.this, response.getMessage(), Snackbar.LENGTH_LONG);
                            layout_progress_appointment.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        layout_progress_appointment.setVisibility(View.GONE);
                        Utility.showErrorMessage(BookingActivity.this, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        layout_progress_appointment.setVisibility(View.GONE);
                        Utility.showErrorMessage(BookingActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);
                    }
                });
            } else {
                Utility.showErrorMessage(this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                layout_progress_appointment.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    void sendMessage(String mobileNumber, String message) {
        OTPServices.getInstance(BookingActivity.this).SendOTP(mobileNumber, message, new ApiStatusCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(BookingActivity.this, "send message", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(BookingActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnknownError(Exception e) {
                Toast.makeText(BookingActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void saveNotificationToSqlite(Notification notification){
        NotificationSqliteOperations notificationSqliteOperations=new NotificationSqliteOperations(BookingActivity.this);
        notificationSqliteOperations.saveNotification(notification);
    }*/


}


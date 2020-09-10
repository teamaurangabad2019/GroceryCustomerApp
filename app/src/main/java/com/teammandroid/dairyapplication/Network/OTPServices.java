package com.teammandroid.dairyapplication.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;

public class OTPServices {

    private static final String TAG = OTPServices.class.getSimpleName();

    private Context context;

    public OTPServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static OTPServices instance;

    public static OTPServices getInstance(Context context) {
        if (instance == null) {
            instance = new OTPServices(context);
        }
        return instance;
    }

    public void SendOTP(String mobile, String message, final ApiStatusCallBack apiStatusCallBack) {
        try {

            //AndroidNetworking.post("http://sms1.teammandroid.com/app/smsapi/index.php?key=55D8E0221DF56A&routeid=468&type=text&contacts="+mobile+"&senderid=Srvdy"+"&msg="+message+"")
            AndroidNetworking.post("http://sms.teammandroid.in/app/smsapi/index.php?key=45EAE47034B550&routeid=468&type=text&contacts="+mobile+"&senderid=SARVOD&msg="+message+"")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            Response response1=new Response(1, "Successfull", 0);
                            apiStatusCallBack.onSuccess(response1);
                        }

                        @Override
                        public void onError(ANError anError) {
                            apiStatusCallBack.onError(anError);
                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }
}

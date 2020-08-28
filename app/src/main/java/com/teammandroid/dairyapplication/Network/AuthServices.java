package com.teammandroid.dairyapplication.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthServices {

    private static final String TAG = AuthServices.class.getSimpleName();

    private Context context;

    public AuthServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static AuthServices  instance;

    public static AuthServices  getInstance(Context context) {
        if (instance == null) {
            instance = new AuthServices (context);
        }
        return instance;
    }

    public void GetUserDetails(int Userid, final ApiStatusCallBack apiStatusCallBack) {

        // {"type":2,"Action":2, "Userid":3}
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 2);
                jsonObject.put("Userid", Userid);
            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_LOGIN)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<UserModel>> token = new TypeToken<ArrayList<UserModel>>() {
                                };
                                ArrayList<UserModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("UserModel", "" + notesPackages);
                                apiStatusCallBack.onSuccess(notesPackages);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("GetUser:anError", "" + anError);
                            Log.e("GetUser:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });

        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }


}

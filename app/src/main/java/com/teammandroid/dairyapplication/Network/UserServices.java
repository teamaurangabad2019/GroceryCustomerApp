package com.teammandroid.dairyapplication.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserServices {
    private static final String TAG = UserServices.class.getSimpleName();

    private Context context;

    public UserServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static UserServices instance;

    public static UserServices getInstance(Context context) {
        if (instance == null) {
            instance = new UserServices(context);
        }
        return instance;
    }

    public void GetAllDeliveryBoyDetails(int Roleid, final ApiStatusCallBack apiStatusCallBack) {

      //  {"type":2,"Action":3,"Roleid":14}
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 3);
                jsonObject.put("Roleid", Roleid);

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
                                ArrayList<UserModel> userDetails = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("AllUserModel", "" + userDetails);

                              //  UserModel userModel=userDetails.get(0);
                                apiStatusCallBack.onSuccess(userDetails);


                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.e("NotesPkgs:anError", "" + anError);
                            Log.e("NotesPkgs:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);

                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }

    public void getDeliveryBoy( final ApiStatusCallBack apiStatusCallBack) {

        //  {"type":2,"Action":3, "Roleid":2} //All deliveryboy
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 3);
                jsonObject.put("Roleid", 2);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_USER)
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
                                ArrayList<UserModel> userDetails = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("AllUserModel", "" + userDetails);

                                //  UserModel userModel=userDetails.get(0);
                                apiStatusCallBack.onSuccess(userDetails);


                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.e("NotesPkgs:anError", "" + anError);
                            Log.e("NotesPkgs:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);

                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }


}

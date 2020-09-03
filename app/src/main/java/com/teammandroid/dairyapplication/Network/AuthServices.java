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
import com.teammandroid.dairyapplication.model.Response;
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


    public void adminLogin(String Mobile,
                           final ApiStatusCallBack apiStatusCallBack) {

        //{"type":2,"Mobile":"11111111111"}

        try {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("type","2");
                jsonObject.put("Mobile",Mobile);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_AUTH)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("responce",""+response);

                            try {
                                TypeToken<Response> token = new TypeToken<Response>() {
                                };
                                Response response1 = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("AUTH", "" + response1);
                                apiStatusCallBack.onSuccess(response1);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("AUTH", e.getMessage());
                            }

                            Log.e("AUTH", String.valueOf(response));

                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("AUTH:anError", "" + anError);
                            Log.e("AUTH:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);

                        }
                    });
        }

        catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }

    }

    public void getAdminInfo(int Userid, final ApiStatusCallBack apiStatusCallBack) {

        // {"type":3,"Action":2,"Userid":1}
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 3);
                jsonObject.put("Action", 2);
                jsonObject.put("Userid", Userid);
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


    public void getUserInfo(int Userid, final ApiStatusCallBack apiStatusCallBack) {

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

            AndroidNetworking.post(Constants.URL_AUTH)
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

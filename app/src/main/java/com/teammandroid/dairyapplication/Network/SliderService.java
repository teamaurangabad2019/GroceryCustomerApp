package com.teammandroid.dairyapplication.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;

import com.teammandroid.dairyapplication.model.SliderModel;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SliderService {

        private static final String TAG = SliderService.class.getSimpleName();

        private Context context;

        public SliderService(Context context) {
            this.context = context;
        }

        @SuppressLint("StaticFieldLeak")
        private static SliderService instance;

        public static SliderService getInstance(Context context) {
            if (instance == null) {
                instance = new SliderService(context);
            }
            return instance;
        }

        public void GetSlides(int id, final ApiStatusCallBack apiStatusCallBack) {

                // {"type":2,"Action":1,"VideoPackageId":1,"VideoId":0,"Offset":0,"Limit":100,"Search":""}
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", 2);
                    jsonObject.put("Action", 1);

                    // {"type":2,"Action":1,"NotesId":0,"NotespackageId":1,"Offset":0,"Limit":100,"Search":""}

                    try {

                    } catch (Exception e) {
                        Log.e("JSONOBJECTerr", "" + e);
                        apiStatusCallBack.onUnknownError(e);
                    }

                    AndroidNetworking.post(Constants.GET_SLIDE)
                            .addJSONObjectBody(jsonObject)
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    try {
                                        TypeToken<ArrayList<SliderModel>> token = new TypeToken<ArrayList<SliderModel>>() {
                                        };
                                        ArrayList<SliderModel> slidePackages = new Gson().fromJson(response.toString(), token.getType());
                                        Log.e("SlidePkgs", "" + slidePackages);
                                        apiStatusCallBack.onSuccess(slidePackages);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("error", e.getMessage());
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e("SlidePkgs:anError", "" + anError);
                                    Log.e("SlidePkgs:anError", "" + anError.getErrorBody());
                                    apiStatusCallBack.onError(anError);

                                }
                            });
                } catch (Exception ex) {
                    Log.e("onUnknownError", "" + ex);
                    apiStatusCallBack.onUnknownError(ex);
                }

        }
}

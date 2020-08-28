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
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryServices {

    private static final String TAG = CategoryServices.class.getSimpleName();

    private Context context;

    public CategoryServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static CategoryServices instance;

    public static CategoryServices getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryServices(context);
        }
        return instance;
    }



    public void fetchCategory(
                              final ApiStatusCallBack apiStatusCallBack) {

        //{"type":2,"Action":1}

        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 1);

               // jsonObject.put("Subjectid", Subjectid);
            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_CATEGORY)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<CategoryModel>> token = new TypeToken<ArrayList<CategoryModel>>() {
                                };
                                ArrayList<CategoryModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("categoryServices", "" + notesPackages);
                                apiStatusCallBack.onSuccess(notesPackages);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("categoryServices", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("HW:anError", "" + anError);
                            Log.e("HW:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });

        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }



}

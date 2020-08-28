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
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductServices {

    private static final String TAG = ProductServices.class.getSimpleName();

    private Context context;

    public ProductServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static ProductServices instance;

    public static ProductServices getInstance(Context context) {
        if (instance == null) {
            instance = new ProductServices(context);
        }
        return instance;
    }



    public void fetchProduct(int Subcategoryid, final ApiStatusCallBack apiStatusCallBack) {

        //{"type":2,"Action":3,"Subcategoryid":3}
        //Subcategoryid
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 1);
                jsonObject.put("Subcategoryid", Subcategoryid);

               // jsonObject.put("Subjectid", Subjectid);
            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_Product)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<ProductModel>> token = new TypeToken<ArrayList<ProductModel>>() {
                                };
                                ArrayList<ProductModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
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

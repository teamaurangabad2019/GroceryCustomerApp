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
import com.teammandroid.dairyapplication.admin.model.OrderDeliveryboyModel;
import com.teammandroid.dairyapplication.admin.model.OrderproductModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.OrderModel;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderProductServices {

    private static final String TAG = OrderProductServices.class.getSimpleName();

    private Context context;

    public OrderProductServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static OrderProductServices instance;

    public static OrderProductServices getInstance(Context context) {
        if (instance == null) {
            instance = new OrderProductServices(context);
        }
        return instance;
    }


    public void fetchProductUsingOrderId(int Orderid,final ApiStatusCallBack apiStatusCallBack) {

        //{"type":2,"Action":5, "Deliveryboyid":35}      fetch all order history using Deliveryboyid

        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 5);
                jsonObject.put("Orderid", Orderid);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER_PRODUCT)
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
                                Log.e("SubjectModel", "" + notesPackages);
                                apiStatusCallBack.onSuccess(notesPackages);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("GetSubject:anError", "" + anError);
                            Log.e("GetSubject:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });

        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }


    public void insertOrderProduct(int Orderproductid,int Orderid, int Productid, int Quantity,double Totalamount,int LogedinUserId, final ApiStatusCallBack apiStatusCallBack) {

        //  add {"type":1,"Action":1, "Orderproductid":0,"Orderid":1,"Productid":1}

        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 1);
                jsonObject.put("Action", 1);
                jsonObject.put("Orderproductid", Orderproductid);
                jsonObject.put("Orderid", Orderid);
                jsonObject.put("Productid", Productid);
                jsonObject.put("Quantity", Quantity);
                jsonObject.put("Totalamount", Totalamount);
                jsonObject.put("LogedinUserId", LogedinUserId);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER_PRODUCT)
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
                                apiStatusCallBack.onSuccess(response1);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("erroe", e.getMessage());
                            }

                            Log.e("orderdetails", String.valueOf(response));

                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("OrderPkgs:anError", "" + anError);
                            Log.e("OrderPkgs:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }


}

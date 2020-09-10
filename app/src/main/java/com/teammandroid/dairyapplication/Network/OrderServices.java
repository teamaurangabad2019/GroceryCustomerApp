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
import com.teammandroid.dairyapplication.admin.model.DeliveryboyStatusModel;
import com.teammandroid.dairyapplication.admin.model.OrderDeliveryboyModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.OrderModel;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderServices {

    private static final String TAG = OrderServices.class.getSimpleName();

    private Context context;

    public OrderServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static OrderServices instance;

    public static OrderServices getInstance(Context context) {
        if (instance == null) {
            instance = new OrderServices(context);
        }
        return instance;
    }

    public void FetchOrder(final ApiStatusCallBack apiStatusCallBack) {
        //{"type":2,"Action":1}  //All order
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 1);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<OrderModel>> token = new TypeToken<ArrayList<OrderModel>>() {
                                };
                                ArrayList<OrderModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
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

    public void createOrder(int Orderid, int Userid,int Status,int Deliveryboyid,String Deliverydate,
                            String Address,int Paymentmode,double Totalprice,double Savedprice,
                            final ApiStatusCallBack apiStatusCallBack) {

        //{"type":1,"Action":1,"Orderid":0, "Status":1,"Deliveryboyid":1,"Deliverydate":"Deliverydate",
        //"Address":"Address","Paymentmode":1,"Totalprice":1,"Savedprice":1, "LogedinUserId":1}
        try {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("type","1");
                jsonObject.put("Action","1");
                jsonObject.put("Userid",Userid);
                jsonObject.put("Orderid",Orderid);
                jsonObject.put("Status",Status);
                jsonObject.put("Deliveryboyid",Deliveryboyid);
                jsonObject.put("Deliverydate",Deliverydate);
                jsonObject.put("Address",Address);
                jsonObject.put("Paymentmode",Paymentmode);
                jsonObject.put("Totalprice",Totalprice);
                jsonObject.put("Savedprice",Savedprice);
                jsonObject.put("LogedinUserId","0");
            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
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
                                Log.e("orderDetails", "" + response1);
                                apiStatusCallBack.onSuccess(response1);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("erroeorder", e.getMessage());
                            }

                            Log.e("orderdetails", String.valueOf(response));

                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("order:anError", "" + anError);
                            Log.e("order:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);

                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }

    }

    public void insertOrderDetails(int Orderid,int Status, int Deliveryboyid,
                                   String Deliverydate, String Address
            , int Paymentmode, double Totalprice, double Savedprice, int LogedinUserId, final ApiStatusCallBack apiStatusCallBack) {

        //   {"type":1,"Action":1,"Orderid":0, "Status":1,"Deliveryboyid":1,"Deliverydate":"Deliverydate",
        //       "Address":"Address","Paymentmode":1,"Totalprice":1,"Savedprice":1, "LogedinUserId":1}

        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 1);
                jsonObject.put("Action", 1);
                jsonObject.put("Orderid", Orderid);
                jsonObject.put("Status", Status);
                jsonObject.put("Deliveryboyid", Deliveryboyid);
                jsonObject.put("Deliverydate", Deliverydate);
                jsonObject.put("Address", Address);
                jsonObject.put("Paymentmode", Paymentmode);
                jsonObject.put("Totalprice", Totalprice);
                jsonObject.put("Savedprice", Savedprice);
                jsonObject.put("LogedinUserId", LogedinUserId);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
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


    public void fetchOrder(int Action, int Deliveryboyid,final ApiStatusCallBack apiStatusCallBack) {

        // {"type":2,"Action":4, "Deliveryboyid":0}
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", Action);
                jsonObject.put("Deliveryboyid", Deliveryboyid);

                // jsonObject.put("Subjectid", Subjectid);
            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<OrderModel>> token = new TypeToken<ArrayList<OrderModel>>() {
                                };
                                ArrayList<OrderModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
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

    public void FetchOrderusingDeliveryboy(int Deliveryboyid,final ApiStatusCallBack apiStatusCallBack) {
        //{"type":2,"Action":5, "Deliveryboyid":35} //All order
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 5);
                jsonObject.put("Deliveryboyid", Deliveryboyid);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<DeliveryboyStatusModel>> token = new TypeToken<ArrayList<DeliveryboyStatusModel>>() {
                                };
                                ArrayList<DeliveryboyStatusModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("DeliveryboyStatusModel", "" + notesPackages);
                                apiStatusCallBack.onSuccess(notesPackages);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("OrderusingDB:anError", "" + anError);
                            Log.e("OrderusingDB:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });

        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }

    public void FetchOrderusingUserid(int Userid,final ApiStatusCallBack apiStatusCallBack) {
        //{"type":2,"Action":5, "Deliveryboyid":35} //All order
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("Action", 6);
                jsonObject.put("Userid", Userid);

            } catch (Exception e) {
                Log.e("JSONOBJECTerr", "" + e);
                apiStatusCallBack.onUnknownError(e);
            }

            AndroidNetworking.post(Constants.URL_ORDER)
                    .addJSONObjectBody(jsonObject)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                TypeToken<ArrayList<DeliveryboyStatusModel>> token = new TypeToken<ArrayList<DeliveryboyStatusModel>>() {
                                };
                                ArrayList<DeliveryboyStatusModel> notesPackages = new Gson().fromJson(response.toString(), token.getType());
                                Log.e("DeliveryboyStatusModel", "" + notesPackages);
                                apiStatusCallBack.onSuccess(notesPackages);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("OrderusingDB:anError", "" + anError);
                            Log.e("OrderusingDB:anError", "" + anError.getErrorBody());
                            apiStatusCallBack.onError(anError);
                        }
                    });

        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }


}

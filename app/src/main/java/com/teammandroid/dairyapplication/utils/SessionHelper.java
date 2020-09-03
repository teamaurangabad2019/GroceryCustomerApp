package com.teammandroid.dairyapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.teammandroid.dairyapplication.activities.AuthenticationActivity;
import com.teammandroid.dairyapplication.model.UserModel;

import static android.accounts.AccountManager.KEY_USERDATA;

/**
 * Created by Ganesh on 2/28/2017.
 */

public class SessionHelper {
    // LogCat tag
    private static String TAG = SessionHelper.class.getSimpleName();

    // Shared Preferences
    static SharedPreferences mInstance;

    static SessionHelper mSessionHelper;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    SharedPreferences pref;
    // Shared preferences file name
    private static final String PREF_NAME = "Dairy_App_Pre";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String TAG_TOKEN = "saarthi_app";
    public static final String KEY_USERDATA = "USERDATA";

    /*public SessionHelper(Context context) {
        this._context = context;
        mInstance = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mInstance.edit();

        if(!mInstance.contains(KEY_IS_LOGGEDIN)) {
            editor.putBoolean(KEY_IS_LOGGEDIN, false);
            editor.putString(TAG_TOKEN, "No Token");
            editor.commit();
        }
    }*/



    // Constructor
    public SessionHelper(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }
    public void setLogin(boolean isLoggedIn) {
        // editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setLogout() {
        editor.clear();
        editor.commit();
        Log.d(TAG, "User logout from session!");
    }

    public boolean isLoggedIn(){
        //return mInstance.getBoolean(KEY_IS_LOGGEDIN, false);
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createLoginSession(UserModel user){
        editor.putBoolean(IS_LOGIN, true);
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString(KEY_USERDATA, json);
            editor.commit();
        } catch (Exception ex) {
            Log.e(TAG, "setStudentInSharedPref: ", ex);
        }
    }


    /*public UserModel getUserDetails(){
        UserModel user = null;

        try {
            String response =  pref.getString(KEY_USERDATA, null);
            Gson gson = new Gson();
            user = gson.fromJson(response, UserModel.class);
        } catch (Exception ex) {
            Log.e(TAG, "getStudentFromSharedPref: ", ex);
        }
        return user;
    }*/

    public UserModel getUserDetails(){
        UserModel user = null;

        try {
            String response =  pref.getString(KEY_USERDATA, null);
            Gson gson = new Gson();
            user = gson.fromJson(response, UserModel.class);
        } catch (Exception ex) {
            Log.e(TAG, "getStudentFromSharedPref: ", ex);
        }
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.remove(KEY_USERDATA);
        // editor.remove(KEY_RECENTVIDEOWATCH);
        editor.remove(IS_LOGIN);

        editor.commit();

        Intent i = new Intent(_context, AuthenticationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


}

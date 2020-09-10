package com.teammandroid.dairyapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    public static int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "successacademy";
    // All Shared Preferences Keys
    private static final String USER_ID = "u_id";
    private static final String USER_MOBILE = "mobile";
    private static final String ROLE_ID = "role_id";
    private static final String COUNT_ID = "count_id";
    private static final String ProfilePath = "ProfilePath";

    private static final String D_BOY_ID = "D_BOY_ID";
    private static final String AUTH_BACK = "auth_back";
    private static final String our_price = "our_price";
    private static final String saved_price = "saved_price";
    private static final String address = "address";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setROLE_ID(int uid) {
        editor.putInt(ROLE_ID, uid);
        editor.commit();
    }

    public int getROLE_ID() {
        return pref.getInt(ROLE_ID, 0);
    }


   public void setCOUNT_ID(int uid) {
        editor.putInt(COUNT_ID, uid);
        editor.commit();
    }

    public int getCOUNT_ID() {
        return pref.getInt(COUNT_ID, 0);
    }


    //
    public void setUSER_ID(int uid) {
        editor.putInt(USER_ID, uid);
        editor.commit();
    }

    public int getUSER_ID() {
        return pref.getInt(USER_ID, 0);
    }


    //
    public void setUSER_MOBILE(String m) {
        editor.putString(USER_MOBILE, m);
        editor.commit();
    }

    public String getUSER_MOBILE() {
        return pref.getString(USER_MOBILE, null);
    }


     public void setProfilePath(String ProfilePath) {
        editor.putString(ProfilePath, ProfilePath);
        editor.commit();
    }

    public String getProfilePath() {
        return pref.getString(ProfilePath, null);
    }


    //

    //
    public void setD_BOY_ID(int p) {
        editor.putInt(D_BOY_ID, p);
        editor.commit();
    }

    public int getD_BOY_ID() {
        return pref.getInt(D_BOY_ID, 0);
    }

    public void setAUTH_BACK(int uid) {
        editor.putInt(AUTH_BACK, uid);
        editor.commit();
    }

    public int getAUTH_BACK() {
        return pref.getInt(AUTH_BACK, 0);
    }

    public void setour_price(String op) {
        editor.putString(our_price, op);
        editor.commit();
    }

    public String getour_price() {
        return pref.getString(our_price, null);
    }

    public void setsaved_price(String op) {
        editor.putString(saved_price, op);
        editor.commit();
    }

    public String getsaved_price() {
        return pref.getString(saved_price, null);
    }

    public void setaddress(String op) {
            editor.putString(address, op);
            editor.commit();
        }

        public String getaddress() {
            return pref.getString(address, null);
        }



}
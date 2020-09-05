package com.teammandroid.dairyapplication.offline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "grocery.db";

    public final static String TABLE_PRODUCT = "product";

    public final static String CUST_1 = "autoid";
    public final static String CUST_2 = "productid";
    public final static String CUST_3 = "title";
    public final static String CUST_4 = "details";
    public final static String CUST_5 = "price";
    public final static String CUST_6 = "ourprice";
    public final static String CUST_7 = "offer";
    public final static String CUST_8 = "isavailable";
    public final static String CUST_9 = "subcategory";
    public final static String CUST_10 = "image";
    public final static String CUST_11 = "isactive";
    public final static String CUST_12 = "created";
    public final static String CUST_13 = "createdby";
    public final static String CUST_14 = "modified";
    public final static String CUST_15 = "modifiedby";
    public final static String CUST_16 = "RowCount";
    public final static String CUST_17 = "userid";

    public final static String TABLE_ORDERS = "orders";

    public final static String ORD_1 = "orderid";
    public final static String ORD_2 = "userid";
    public final static String ORD_3 = "productname";
    public final static String ORD_4 = "productquantity";
    public final static String ORD_5 = "amountpayable";
    public final static String ORD_6 = "deliverydate";
    public final static String ORD_7 = "deliverypersonId";//dboyid
    public final static String ORD_8 = "isactive";


    public final static String TABLE_DROP_ORDER = "droporder";

    public final static String DRP_ORD_1 = "droporderid";
    public final static String DRP_ORD_2 = "userid";
    public final static String DRP_ORD_3 = "dboyid";//dboyid
    public final static String DRP_ORD_4 = "startdate";
    public final static String DRP_ORD_5 = "enddate";
    public final static String DRP_ORD_6 = "isactive";


    public final static String TABLE_PAYMENT = "payment";

    public final static String PAY_1 = "paymentid";
    public final static String PAY_2 = "userid";
    public final static String PAY_3 = "amount";
    public final static String PAY_4 = "pdate";
    public final static String PAY_5 = "month";
    public final static String PAY_6 = "deliverypersonId";

    public final static String TABLE_QUANTITY = "quantity";

    public final static String QUANTITY_1 = "quantityid";
    public final static String QUANTITY_2 = "productid";
    public final static String QUANTITY_3 = "userid";
    public final static String QUANTITY_4 = "count";

    public final static String TABLE_PLUS_MINUS_QUANTITY = "plusminusquantity";

    public final static String PMQUANTITY_1 = "pmquantityid";
    public final static String PMQUANTITY_2 = "pmproductid";
    public final static String PMQUANTITY_3 = "pmuserid";
    public final static String PMQUANTITY_4 = "pmcount";
    public final static String PMQUANTITY_5 = "pmamount";




    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_PRODUCT+
                " (autoid INTEGER PRIMARY KEY AUTOINCREMENT," + //1
                "productid INTEGER," +                      //2
                "title TEXT," +                            //3
                "details TEXT," +                            //3
                "price REAL," +                        //4
                "ourprice REAL," +                        //4//5
                "offer INTEGER," +                        //6//6
                "isavailable INTEGER," +                        //6//6
                "subcategory INTEGER," +                        //6//7
                "image TEXT ,"+
                "isactive INTEGER," +
                "created TEXT," +
                "createdby INTEGER," +
                "modified TEXT," +
                "modifiedby INTEGER," +
                "RowCount INTEGER,"+                          //10
                "userid INTEGER )");                          //10


        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_ORDERS+
                " (orderid INTEGER PRIMARY KEY AUTOINCREMENT," + //1
                "userid INTEGER," +                      //2
                "productname TEXT," +                            //3
                "productquantity INTEGER," +                        //4
                "amountpayable REAL," +                           //5
                "deliverydate TEXT," +                        //6
                "deliverypersonId INTEGER," +                              //7
                "isactive INTEGER)");                        //8

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_QUANTITY +
                " (quantityid INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "productid INTEGER," +
                "userid INTEGER," +
                "count INTEGER)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_PLUS_MINUS_QUANTITY +
                " (pmquantityid INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "pmproductid INTEGER," +
                "pmuserid INTEGER," +
                "pmcount INTEGER," +
                "pmamount REAL)" );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DROP_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUANTITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLUS_MINUS_QUANTITY);
    }

    //to insert data inside ProductInfo table
    public boolean insertProductInfo(int productid,String title,String details,
                                     double price,double ourprice,
                                     int offer,int subcategory,String image ,int isavailable,int isactive
            ,String created,int createdby,String modified,int modifiedby,int RowCount, int userid)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //price,subcategory,title,offer,image,ourprice,productid,details
        contentValues.put(CUST_5,price);
        contentValues.put(CUST_9,subcategory);
        contentValues.put(CUST_3,title);
        contentValues.put(CUST_7,offer);
        contentValues.put(CUST_10,image);
        contentValues.put(CUST_6,ourprice);
        contentValues.put(CUST_2,productid);
        contentValues.put(CUST_4,details);
        contentValues.put(CUST_8,isavailable);
        contentValues.put(CUST_11,isactive);
        contentValues.put(CUST_12,created);
        contentValues.put(CUST_13,createdby);
        contentValues.put(CUST_14,modified);
        contentValues.put(CUST_15,modifiedby);
        contentValues.put(CUST_16,RowCount);
        contentValues.put(CUST_17,userid);

        //long result = db.insert(TABLE_PRODUCT, null, contentValues);

        long result = db.insert(TABLE_PRODUCT, null, contentValues);

        if (result == -1){
            return false;
        } else {
            return true;
        }

    }

    //AlreadyExistEntry
    public boolean alreadyExistProductEntry(int productId) {

        // array of columns to fetch
        String[] columns = {
                QUANTITY_2
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria


        String selection = QUANTITY_2 + " = ?";
        //String selection = CUST_2 + " = ?" + " AND " + ORD_6 + " = ?";

        // selection arguments
        String[] selectionArgs = {Integer.toString(productId)};

        Cursor cursor = db.rawQuery("SELECT " + QUANTITY_2 +" FROM " + TABLE_QUANTITY +" WHERE "+ QUANTITY_2 +" = "+ productId,null);
        int cursorCount = cursor.getCount();

        Log.d("DATABASEHELPER ",String.valueOf(cursorCount));

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public boolean insertQuantity(int productid,int userid,int count)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(QUANTITY_2,productid);
        contentValues.put(QUANTITY_3,userid);
        contentValues.put(QUANTITY_4,count);

        long result=db.insert(TABLE_QUANTITY,null,contentValues);

        if(result == -1) {
            return  false;
        } else {
            return true;
        }
    }

    public boolean insertPmQuantity(int productid,int userid,int count,double amount)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(PMQUANTITY_2,productid);
        contentValues.put(PMQUANTITY_3,userid);
        contentValues.put(PMQUANTITY_4,count);
        contentValues.put(PMQUANTITY_5,amount);

        long result=db.insert(TABLE_PLUS_MINUS_QUANTITY,null,contentValues);

        if(result == -1) {
            return  false;
        } else {
            return true;
        }
    }



    public boolean UpdateData(String custid,String fullname,String mobile,String address,
                              int quantity,double amount,
                              double amountfor1quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUST_6, quantity);
        contentValues.put(CUST_4, address);
        contentValues.put(CUST_2, fullname);
        contentValues.put(CUST_7, amount);
        contentValues.put(CUST_1, custid);
        contentValues.put(CUST_3, mobile);


        db.update(TABLE_PRODUCT,contentValues,"custid=?",new String[]{custid});//new String[]{ id });
        return  true;
    }

    public Cursor getData(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM "+TABLE_PRODUCT+" WHERE custid='"+id+"'";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PRODUCT, null);
        return cursor;
    }



    //to delete quantity
    public Integer deleteQuantity(int quantityId){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_QUANTITY, "quantityid=?", new String[]{String.valueOf(quantityId)});

        //return db.delete()
    }


    public Cursor getQuantityData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_QUANTITY, null);
        return cursor;
    }



    public ArrayList getAllCotacts(String  userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select * from "+TABLE_PRODUCT+" where userid= "+ userid, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            //  array_list.add(res.getString(res.getColumnIndex(CUST_1)));
            array_list.add(res.getString(res.getColumnIndex(CUST_2)));
          /*  array_list.add(res.getString(res.getColumnIndex(CUST_3)));
            array_list.add(res.getString(res.getColumnIndex(CUST_4)));
            array_list.add(res.getString(res.getColumnIndex(CUST_5)));
            array_list.add(res.getString(res.getColumnIndex(CUST_6)));
            array_list.add(res.getString(res.getColumnIndex(CUST_7)));
            array_list.add(res.getString(res.getColumnIndex(CUST_8)));
            array_list.add(res.getString(res.getColumnIndex(CUST_9)));
            array_list.add(res.getString(res.getColumnIndex(CUST_10)));
            array_list.add(res.getString(res.getColumnIndex(CUST_11)));
            array_list.add(res.getString(res.getColumnIndex(CUST_12)));
            array_list.add(res.getString(res.getColumnIndex(CUST_13)));
            array_list.add(res.getString(res.getColumnIndex(CUST_14)));
            array_list.add(res.getString(res.getColumnIndex(CUST_15)));
            array_list.add(res.getString(res.getColumnIndex(CUST_16)));
            array_list.add(res.getString(res.getColumnIndex(CUST_17)));
          */  res.moveToNext();
        }
        return array_list;
    }
}



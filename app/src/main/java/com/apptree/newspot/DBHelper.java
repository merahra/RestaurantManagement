package com.apptree.newspot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_Name = "spot.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createTables(SQLiteDatabase data) {
        String completed_order_table_sql = "create table completed_order ( _id integer  primary key autoincrement,completed_order_name BLOB,completed_order_qty BLOB,completed_order_price FLOAT,completed_order_date TEXT,completed_order_payment_reference_no TEXT,completed_order_table_no TEXT,completed_order_mode TEXT,completed_order_card_no TEXT)";
        String order_table_sql = "create table order_placed ( _id integer  primary key autoincrement,order_name BLOB,order_qty BLOB,order_price FLOAT,order_date TEXT,order_payment_reference_no TEXT,order_table_no TEXT,order_mode TEXT,order_card_no TEXT)";
        String admin_table_query_sql = "create table admin_login ( _id integer primary key autoincrement,admin_user_name TEXT,admin_pass TEXT)";
        try {
            data.execSQL("create table menu ( _id integer  primary key autoincrement,item_name TEXT,category TEXT,description TEXT,price FLOAT,image_path TEXT)");
            data.execSQL(order_table_sql);
            data.execSQL(completed_order_table_sql);
            data.execSQL(admin_table_query_sql);
            Log.d("MENU", "Tables created!");
            Log.d("ORDER", "Tables created!");
            Log.d("COMPLETED ORDER", "Tables created!");
            Log.d("ADMIN LOGIN", "Tables created!");
        } catch (Exception e) {
            Log.d("MENU", "Error in DBHelper.onCreate() : " + e.getMessage());
        }
    }
}

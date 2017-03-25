package com.apptree.newspot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;

public class Database {
    public static final String ADMIN_LOGIN_ID = "_id";
    public static final String ADMIN_LOGIN_NAME = "admin_user_name";
    public static final String ADMIN_LOGIN_PASS = "admin_pass";
    public static final String ADMIN_LOGIN_TABLE_NAME = "admin_login";
    public static final String COMPLETED_ORDER_CARD_NO = "completed_order_card_no";
    public static final String COMPLETED_ORDER_DATE = "completed_order_date";
    public static final String COMPLETED_ORDER_ID = "_id";
    public static final String COMPLETED_ORDER_MODE = "completed_order_mode";
    public static final String COMPLETED_ORDER_NAME = "completed_order_name";
    public static final String COMPLETED_ORDER_PAYMENT_REFERENCE_NO = "completed_order_payment_reference_no";
    public static final String COMPLETED_ORDER_PRICE = "completed_order_price";
    public static final String COMPLETED_ORDER_QTY = "completed_order_qty";
    public static final String COMPLETED_ORDER_TABLE_NAME = "completed_order";
    public static final String COMPLETED_ORDER_TABLE_NO = "completed_order_table_no";
    public static final String ITEM_CATEGORY = "category";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_ID = "_id";
    public static final String ITEM_IMAGE_PATH = "image_path";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_PRICE = "price";
    public static final String MENU_TABLE_NAME = "menu";
    public static final String ORDER_CARD_NO = "order_card_no";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_ID = "_id";
    public static final String ORDER_MODE = "order_mode";
    public static final String ORDER_NAME = "order_name";
    public static final String ORDER_PAYMENT_REFERENCE_NO = "order_payment_reference_no";
    public static final String ORDER_PRICE = "order_price";
    public static final String ORDER_QTY = "order_qty";
    public static final String ORDER_TABLE_NAME = "order_placed";
    public static final String ORDER_TABLE_NO = "order_table_no";
    private static int day;
    private static int month;
    private static int year;

    public static ArrayList<MenuPojo> fillViewItemsList(ListView list) {
        Context ctx = list.getContext();
        ArrayList<MenuPojo> viewItems = new ArrayList();
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(MENU_TABLE_NAME, null, null, null, null, null, ITEM_CATEGORY);
        while (c.moveToNext()) {
            viewItems.add(cursorToViewItems(c));
        }
        c.close();
        db.close();
        dbHelper.close();
        return viewItems;
    }

    private static MenuPojo cursorToViewItems(Cursor c) {
        MenuPojo mp = new MenuPojo();
        mp.setItemId(c.getString(c.getColumnIndex(ORDER_ID)));
        mp.setItemName(c.getString(c.getColumnIndex(ITEM_NAME)));
        mp.setItemDesc(c.getString(c.getColumnIndex(ITEM_DESCRIPTION)));
        mp.setItemCat(c.getString(c.getColumnIndex(ITEM_CATEGORY)));
        mp.setItemPrice(c.getString(c.getColumnIndex(ITEM_PRICE)));
        mp.setItemPath(c.getString(c.getColumnIndex(ITEM_IMAGE_PATH)));
        return mp;
    }

    public static ArrayList<SelectedOrderPojo> fillSelectedOrderList(ListView list, ArrayList<String> arrList) {
        Log.d("Database", "Entered");
        Context ctx = list.getContext();
        int i = arrList.size();
        Log.d("Database", Integer.toString(i));
        ArrayList<SelectedOrderPojo> selectedOrderPojoList = new ArrayList();
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;
        for (int a = 0; a < i; a++) {
            Log.d("LOOP", Integer.toString(a));
            Log.d("LOOP", (String) arrList.get(a));
            c = db.query(MENU_TABLE_NAME, null, "_id = ? ", new String[]{str}, null, null, null);
            if (c.moveToFirst()) {
                selectedOrderPojoList.add(cursorToSelectedItems(c));
            }
        }
        c.close();
        db.close();
        dbHelper.close();
        return selectedOrderPojoList;
    }

    public static SelectedOrderPojo cursorToSelectedItems(Cursor c) {
        SelectedOrderPojo sp = new SelectedOrderPojo();
        sp.setOrderId(c.getString(c.getColumnIndex(ORDER_ID)));
        sp.setOrderName(c.getString(c.getColumnIndex(ITEM_NAME)));
        sp.setOrderDesc(c.getString(c.getColumnIndex(ITEM_DESCRIPTION)));
        sp.setOrderCat(c.getString(c.getColumnIndex(ITEM_CATEGORY)));
        sp.setOrderPrice(c.getString(c.getColumnIndex(ITEM_PRICE)));
        sp.setOrderPath(c.getString(c.getColumnIndex(ITEM_IMAGE_PATH)));
        sp.setQty(Integer.toString(1));
        sp.setTotal(Integer.toString(Integer.parseInt(sp.getOrderPrice()) * Integer.parseInt(sp.getQty())));
        sp.setOrderDate(getCurrentDate());
        return sp;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(1);
        month = c.get(2);
        day = c.get(5);
        return String.format("%d-%d-%d", new Object[]{Integer.valueOf(day), Integer.valueOf(month + 1), Integer.valueOf(year)});
    }

    public int totalAmount(String amount, String qty) {
        return 0;
    }
}

package com.apptree.newspot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrders extends Activity {
    private SimpleAdapter adapter;
    private ListView list;
    private ArrayList<HashMap<String, Object>> testList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders);
        this.list = (ListView) findViewById(R.id.list);
        fetchOrders();
        this.list.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View selectedView, int position, long id) {
                String remove = ((TextView) selectedView.findViewById(R.id.orderId)).getText().toString();
                Log.d("LONG CLICK LISTENER", remove);
                ViewOrders.this.removeItemsFromList(remove);
                ViewOrders.this.finish();
                ViewOrders.this.startActivity(new Intent(ViewOrders.this, ViewOrders.class));
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_orders, menu);
        return true;
    }

    public void fetchOrders() {
        Log.d("VIEW ORDERS LIST", "RUNNING");
        ArrayList<String> nameList = new ArrayList();
        ArrayList<String> qtyList = new ArrayList();
        try {
            Log.d("VIEW ORDERS LIST", "Entered Try Block");
            SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
            this.testList = new ArrayList();
            Cursor c = db.query(Database.ORDER_TABLE_NAME, null, null, null, null, null, "_id DESC");
            while (c.moveToNext()) {
                Log.d("VIEW ORDERS LIST", Integer.toString(c.getCount()));
                ByteArrayInputStream bis = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.ORDER_NAME)));
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                nameList = (ArrayList) objectInputStream.readObject();
                ByteArrayInputStream bi = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.ORDER_QTY)));
                objectInputStream = new ObjectInputStream(bi);
                qtyList = (ArrayList) objectInputStream.readObject();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < nameList.size(); i++) {
                    sb.append(new StringBuilder(String.valueOf(Integer.toString(i + 1))).append(". ").append((String) nameList.get(i)).append(" (").append((String) qtyList.get(i)).append(")").append("\n").toString());
                    Log.d("String buffer", sb.toString());
                }
                Log.d("VIEW ORDERS LIST", (String) nameList.get(0));
                Log.d("VIEW ORDERS LIST", (String) qtyList.get(0));
                Log.d("VIEW ORDERS LIST", Integer.toString(qtyList.size()));
                objectInputStream.close();
                objectInputStream.close();
                bis.close();
                bi.close();
                HashMap<String, Object> hMap = new HashMap();
                hMap.put("name", sb);
                hMap.put("orderId", c.getString(c.getColumnIndex(Database.ORDER_ID)));
                hMap.put("date", c.getString(c.getColumnIndex(Database.ORDER_DATE)));
                hMap.put(Database.ITEM_PRICE, c.getString(c.getColumnIndex(Database.ORDER_PRICE)));
                hMap.put("table_no", c.getString(c.getColumnIndex(Database.ORDER_TABLE_NO)));
                hMap.put("bill_no", c.getString(c.getColumnIndex(Database.ORDER_PAYMENT_REFERENCE_NO)));
                this.testList.add(hMap);
            }
            this.adapter = new SimpleAdapter(this, this.testList, R.layout.view_orders_row, new String[]{"name", "date", Database.ITEM_PRICE, "table_no", "bill_no", "orderId"}, new int[]{R.id.tvViewOrderedItems, R.id.tvViewOrderedDate, R.id.tvViewOrderedAmount, R.id.tvViewOrderedTableNo, R.id.tvViewOrderedBillNo, R.id.orderId});
            this.list.setAdapter(this.adapter);
        } catch (Exception e) {
        }
    }

    public void removeItemsFromList(String remove) {
        try {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            Cursor c = db.query(Database.ORDER_TABLE_NAME, null, " _id = ? ", new String[]{remove}, null, null, null);
            Log.d("CURSOR COUNT", Integer.toString(c.getCount()));
            c.moveToFirst();
            byte[] name = c.getBlob(c.getColumnIndex(Database.ORDER_NAME));
            byte[] qty = c.getBlob(c.getColumnIndex(Database.ORDER_QTY));
            Log.d("BEFORE CONTENT VALUES", "running");
            ContentValues cv = new ContentValues();
            cv.put(Database.COMPLETED_ORDER_NAME, name);
            cv.put(Database.COMPLETED_ORDER_QTY, qty);
            cv.put(Database.COMPLETED_ORDER_PRICE, c.getString(c.getColumnIndex(Database.ORDER_PRICE)));
            cv.put(Database.COMPLETED_ORDER_DATE, c.getString(c.getColumnIndex(Database.ORDER_DATE)));
            cv.put(Database.COMPLETED_ORDER_CARD_NO, c.getString(c.getColumnIndex(Database.ORDER_CARD_NO)));
            cv.put(Database.COMPLETED_ORDER_MODE, c.getString(c.getColumnIndex(Database.ORDER_MODE)));
            cv.put(Database.COMPLETED_ORDER_PAYMENT_REFERENCE_NO, c.getString(c.getColumnIndex(Database.ORDER_PAYMENT_REFERENCE_NO)));
            cv.put(Database.COMPLETED_ORDER_TABLE_NO, c.getString(c.getColumnIndex(Database.ORDER_TABLE_NO)));
            db.insert(Database.COMPLETED_ORDER_TABLE_NAME, null, cv);
            Log.d("ROWS DELETED", Integer.toString(db.delete(Database.ORDER_TABLE_NAME, "_id = ? ", new String[]{remove})));
            c.close();
            db.close();
        } catch (Exception e) {
        }
    }
}

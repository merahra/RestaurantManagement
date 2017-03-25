package com.apptree.newspot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CheckOut extends Activity {
    private String cardNo;
    private String date;
    private EditText etCard;
    private EditText etInvoice;
    private EditText etTable;
    private ArrayList<String> nameList;
    private String payMode;
    private ArrayList<String> qtyList;
    private RadioButton rdCard;
    private RadioButton rdCash;
    private RadioGroup rdGroup;
    private int total;
    private TextView totalAmount;
    private TextView tvCard;
    private TextView tvCurrency;
    private TextView tvInvoice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);
        this.rdCash = (RadioButton) findViewById(R.id.rdCash);
        this.rdCard = (RadioButton) findViewById(R.id.rdCard);
        this.rdGroup = (RadioGroup) findViewById(R.id.rdGroup);
        this.tvCard = (TextView) findViewById(R.id.tvCard);
        this.tvInvoice = (TextView) findViewById(R.id.tvInvoice);
        this.tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        this.etCard = (EditText) findViewById(R.id.etCard);
        this.etInvoice = (EditText) findViewById(R.id.etInvoice);
        this.etTable = (EditText) findViewById(R.id.etTable);
        this.total = getIntent().getIntExtra("toalAmount", 0);
        this.date = getIntent().getStringExtra("date");
        this.nameList = getIntent().getStringArrayListExtra("nameList");
        this.qtyList = getIntent().getStringArrayListExtra("qtyList");
        Log.d("CHECK OUT", Integer.toString(this.total));
        this.totalAmount = (TextView) findViewById(R.id.tvPayment);
        this.totalAmount.setText(Integer.toString(this.total));
        this.tvCurrency.setText(getSharedPreferences("currency", 0).getString("curr_symbol", "\u00a3"));
        this.rdGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (CheckOut.this.rdCard.isChecked()) {
                    CheckOut.this.clearAll();
                    CheckOut.this.tvCard.setVisibility(0);
                    CheckOut.this.etCard.setVisibility(0);
                    CheckOut.this.tvInvoice.setVisibility(0);
                    CheckOut.this.etInvoice.setVisibility(0);
                    CheckOut.this.payMode = "Card";
                }
                if (CheckOut.this.rdCash.isChecked()) {
                    CheckOut.this.clearAll();
                    CheckOut.this.tvCard.setVisibility(4);
                    CheckOut.this.etCard.setVisibility(4);
                    CheckOut.this.tvInvoice.setVisibility(0);
                    CheckOut.this.etInvoice.setVisibility(0);
                    CheckOut.this.payMode = "Cash";
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_out, menu);
        return true;
    }

    public void clearAll() {
        this.etCard.setText("");
        this.etInvoice.setText("");
    }

    public void onConfirm(View v) {
        if (this.rdCard.isChecked()) {
            this.cardNo = this.etCard.getText().toString();
        }
        if (this.rdCash.isChecked()) {
            this.cardNo = "";
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this.nameList);
            byte[] nameBuff = bos.toByteArray();
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(this.qtyList);
            byte[] qtyBuff = bo.toByteArray();
            SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Database.ORDER_NAME, nameBuff);
            cv.put(Database.ORDER_QTY, qtyBuff);
            cv.put(Database.ORDER_PRICE, Integer.valueOf(this.total));
            cv.put(Database.ORDER_DATE, this.date);
            cv.put(Database.ORDER_TABLE_NO, this.etTable.getText().toString());
            cv.put(Database.ORDER_CARD_NO, this.cardNo);
            cv.put(Database.ORDER_MODE, this.payMode);
            cv.put(Database.ORDER_PAYMENT_REFERENCE_NO, this.etInvoice.getText().toString());
            db.insert(Database.ORDER_TABLE_NAME, null, cv);
            oos.close();
            oo.close();
            db.close();
            Toast.makeText(this, "Your Order Is Placed", 1).show();
        } catch (Exception e) {
            Toast.makeText(this, "Your Order Is Placed " + e.getMessage(), 1).show();
        }
        finish();
        Intent intent = new Intent(this, DisplayItems.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }
}

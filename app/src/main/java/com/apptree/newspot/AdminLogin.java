package com.apptree.newspot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class AdminLogin extends Activity {
    String adminPassword;
    String adminUserName;
    private EditText etPassword;
    private EditText etUserName;
    String pass;
    TextView tvLoginMsg;
    String userName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        this.etPassword = (EditText) findViewById(R.id.etPassword);
        this.etUserName = (EditText) findViewById(R.id.etUserName);
        this.tvLoginMsg = (TextView) findViewById(R.id.tvLoginMsg);
        this.etPassword.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != R.id.login && actionId != 0) {
                    return false;
                }
                AdminLogin.this.attemptLogin();
                return true;
            }
        });
    }

    public void logIn(View v) {
        attemptLogin();
    }

    public void attemptLogin() {
        Log.d("Attemptlogin", "Running");
        this.etUserName.setError(null);
        this.etPassword.setError(null);
        this.userName = this.etUserName.getText().toString();
        this.pass = this.etPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(this.pass)) {
            this.etPassword.setError(getString(R.string.error_field_required));
            focusView = this.etPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(this.userName)) {
            this.etUserName.setError(getString(R.string.error_field_required));
            focusView = this.etUserName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        Log.d("Attemptlogin", "Running");
        compareFromDatabaseForLogInAttempt();
    }

    public void compareFromDatabaseForLogInAttempt() {
        try {
            SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
            Cursor c = db.query(Database.ADMIN_LOGIN_TABLE_NAME, null, null, null, null, null, null);
            if (c.getCount() == 0) {
                Toast.makeText(this, " No Admin Registered , Please sign Up ", 1).show();
            } else if (c.getCount() != 0) {
                matchNameIfDatabaseNotNull(c, db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void matchNameIfDatabaseNotNull(Cursor c, SQLiteDatabase db) {
        Log.d("Runnning", "udhauihfeiueaf");
        SQLiteDatabase sQLiteDatabase = db;
        c = sQLiteDatabase.query(Database.ADMIN_LOGIN_TABLE_NAME, null, "admin_user_name = ?", new String[]{this.userName}, null, null, null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "Invalid User Name", 1).show();
        } else if (c.getCount() != 0) {
            c.moveToFirst();
            if (c.getString(c.getColumnIndex(Database.ADMIN_LOGIN_PASS)).equals(this.pass)) {
                startActivity(new Intent(this, AdminPanel.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Password", 1).show();
            }
        }
        c.close();
        db.close();
    }

    public void signUp(View v) {
        createNewUser();
    }

    public void createNewUser() {
        this.etUserName.setError(null);
        this.etPassword.setError(null);
        this.adminUserName = this.etUserName.getText().toString();
        this.adminPassword = this.etPassword.getText().toString();
        boolean check = false;
        View checkView = null;
        if (TextUtils.isEmpty(this.adminPassword)) {
            this.etPassword.setError(getString(R.string.error_field_required));
            checkView = this.etPassword;
            check = true;
        }
        if (TextUtils.isEmpty(this.adminUserName)) {
            this.etUserName.setError(getString(R.string.error_field_required));
            checkView = this.etUserName;
            check = true;
        }
        if (check) {
            checkView.requestFocus();
        } else {
            compareFromDatabaseForNewUserCheck(this.adminUserName, this.adminPassword);
        }
    }

    public void compareFromDatabaseForNewUserCheck(String adminUserName, String adminPassword) {
        try {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.query(Database.ADMIN_LOGIN_TABLE_NAME, new String[]{Database.ADMIN_LOGIN_NAME}, "admin_user_name = ?", new String[]{adminUserName}, null, null, null);
            if (c.getCount() == 0) {
                try {
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Database.ADMIN_LOGIN_NAME, adminUserName);
                    values.put(Database.ADMIN_LOGIN_PASS, adminPassword);
                    if (db.insert(Database.ADMIN_LOGIN_TABLE_NAME, null, values) > 0) {
                        Toast.makeText(this, "New Admin Created !!!", 1).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Registration Failed !!!", 1).show();
                    }
                    startActivity(new Intent(this, AdminPanel.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (c.getCount() != 0) {
                Toast.makeText(this, "User Name already exists.", 1).show();
            }
            c.close();
            db.close();
        } catch (Exception e2) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_login, menu);
        return true;
    }
}

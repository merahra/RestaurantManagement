package com.apptree.newspot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateItem extends Activity {
    private static final int RESULT_LOAD_IMAGE = 2;
    private String chosenCategory;
    private EditText etDescription;
    private EditText etItemName;
    private EditText etPrice;
    private String imagePath;
    private ImageView imgItem;
    private Spinner spCategory;
    private String viewItemId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);
        this.etItemName = (EditText) findViewById(R.id.etItemName);
        this.spCategory = (Spinner) findViewById(R.id.spCategory);
        this.etPrice = (EditText) findViewById(R.id.etPrice);
        this.etDescription = (EditText) findViewById(R.id.etDescription);
        this.imgItem = (ImageView) findViewById(R.id.imgItem);
        this.spCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_row));
        this.spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                UpdateItem.this.chosenCategory = UpdateItem.this.spCategory.getSelectedItem().toString();
                Log.d("Spinner", UpdateItem.this.chosenCategory);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.viewItemId = getIntent().getStringExtra("viewItemId");
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(Database.MENU_TABLE_NAME, null, "_id = ?", new String[]{this.viewItemId}, null, null, null);
        if (c.moveToFirst()) {
            this.etItemName.setText(c.getString(c.getColumnIndex(Database.ITEM_NAME)));
            this.etDescription.setText(c.getString(c.getColumnIndex(Database.ITEM_DESCRIPTION)));
            this.etPrice.setText(c.getString(c.getColumnIndex(Database.ITEM_PRICE)));
            this.imgItem.setImageBitmap(BitmapFactory.decodeFile(c.getString(c.getColumnIndex(Database.ITEM_IMAGE_PATH))));
            this.imagePath = c.getString(c.getColumnIndex(Database.ITEM_IMAGE_PATH));
            Log.d("IMAGE PATH FROM DATABASE", this.imagePath);
            this.spCategory.setSelection(((ArrayAdapter) this.spCategory.getAdapter()).getPosition(c.getString(c.getColumnIndex(Database.ITEM_CATEGORY))));
        }
        c.close();
        db.close();
        dbHelper.close();
    }

    public void loadPicture(View v) {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            String[] filePathColumn = new String[]{"_data"};
            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            this.imagePath = picturePath;
            cursor.close();
            this.imgItem.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void saveItem(View v) {
        this.etItemName.setError(null);
        this.etPrice.setError(null);
        this.etDescription.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(this.etItemName.getText().toString())) {
            this.etItemName.setError(getString(R.string.error_field_required));
            focusView = this.etItemName;
            cancel = true;
        }
        if (TextUtils.isEmpty(this.etPrice.getText().toString())) {
            this.etPrice.setError(getString(R.string.error_field_required));
            focusView = this.etPrice;
            cancel = true;
        }
        if (TextUtils.isEmpty(this.etDescription.getText().toString())) {
            this.etDescription.setError(getString(R.string.error_field_required));
            focusView = this.etDescription;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        try {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Database.ITEM_NAME, this.etItemName.getText().toString());
            values.put(Database.ITEM_CATEGORY, this.chosenCategory);
            values.put(Database.ITEM_PRICE, this.etPrice.getText().toString());
            values.put(Database.ITEM_DESCRIPTION, this.etDescription.getText().toString());
            values.put(Database.ITEM_IMAGE_PATH, this.imagePath);
            int rows = db.update(Database.MENU_TABLE_NAME, values, "_id = ?", new String[]{this.viewItemId});
            db.close();
            if (rows > 0) {
                Toast.makeText(this, "Item Updated In Menu !!!", 1).show();
                finish();
                return;
            }
            Toast.makeText(this, "Item Could Not Be Updated !!!", 1).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), 1).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_item, menu);
        return true;
    }
}

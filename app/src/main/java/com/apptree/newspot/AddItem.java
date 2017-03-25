package com.apptree.newspot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItem extends Activity {
    private static final int RESULT_CLICK_IMAGE = 2;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Button btnClickPicture;
    private Button btnLoadPicture;
    String chosenCategory;
    private EditText etDescription;
    private EditText etItemName;
    private EditText etPrice;
    String imagePath;
    private ImageView imgItem;
    private PackageManager pm;
    private Spinner spCategory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        this.btnLoadPicture = (Button) findViewById(R.id.btnLoadPicture);
        this.btnLoadPicture = (Button) findViewById(R.id.btnLoadPicture);
        this.spCategory = (Spinner) findViewById(R.id.spCategory);
        this.etItemName = (EditText) findViewById(R.id.etItemName);
        this.etPrice = (EditText) findViewById(R.id.etPrice);
        this.etDescription = (EditText) findViewById(R.id.etDescription);
        this.imgItem = (ImageView) findViewById(R.id.imgItem);
        BitmapFactory bmp = new BitmapFactory();
        this.spCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_row));
        this.spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                AddItem.this.chosenCategory = AddItem.this.spCategory.getSelectedItem().toString();
                Log.d("Spinner", AddItem.this.chosenCategory);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void loadPicture(View v) {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = new String[RESULT_LOAD_IMAGE];
            filePathColumn[0] = "_data";
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            this.imagePath = picturePath;
            cursor.close();
            this.imgItem.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == RESULT_CLICK_IMAGE && resultCode == -1 && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            this.imgItem.setImageBitmap(photo);
            try {
                String name = createImageFileName();
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "MyImages";
                new File(path).mkdirs();
                File outputFile = new File(path, name);
                this.imagePath = outputFile.getPath();
                Log.d("BITMAP", this.imagePath);
                photo.compress(CompressFormat.PNG, 100, new FileOutputStream(outputFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clickPicture(View v) {
        this.pm = getPackageManager();
        if (this.pm.hasSystemFeature("android.hardware.camera")) {
            startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), RESULT_CLICK_IMAGE);
            return;
        }
        new Toast(this).setGravity(17, 0, 0);
        Toast.makeText(this, "No Rear Camera Available On Device", RESULT_LOAD_IMAGE).show();
    }

    private String createImageFileName() throws IOException {
        return "app" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
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
            long rows = db.insert(Database.MENU_TABLE_NAME, null, values);
            db.close();
            if (rows > 0) {
                Toast.makeText(this, "Item Added In Menu !!!", RESULT_LOAD_IMAGE).show();
                finish();
                return;
            }
            Toast.makeText(this, "Item Could Not Be Added !!!", RESULT_LOAD_IMAGE).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), RESULT_LOAD_IMAGE).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }
}

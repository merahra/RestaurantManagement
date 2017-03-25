package com.apptree.newspot;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteItems extends ListActivity {
    private SimpleCursorAdapter adapter = null;
    private Button btnDelete;
    Cursor c = null;
    private ListView lv = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_items);
        this.lv = getListView();
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        try {
            Cursor c = new DBHelper(this).getReadableDatabase().query(Database.MENU_TABLE_NAME, null, null, null, null, null, null);
            c.moveToFirst();
            if (c.getCount() == 0) {
                this.btnDelete.setVisibility(4);
                Toast.makeText(this, "No Records found", 1).show();
                return;
            }
            Log.d("delete", c.getString(c.getColumnIndex(Database.ITEM_NAME)));
            this.adapter = new SimpleCursorAdapter(this, 17367056, c, new String[]{Database.ITEM_NAME}, new int[]{16908308});
            this.lv.setAdapter(this.adapter);
            this.lv.setChoiceMode(2);
        } catch (Exception e) {
        }
    }

    public void doClick(View v) {
        if (this.adapter.hasStableIds()) {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            long[] viewItems = this.lv.getCheckedItemIds();
            for (long j : viewItems) {
                db.execSQL("delete from menu where _id = " + j);
                finish();
            }
            startActivity(new Intent(this, DeleteItems.class));
            return;
        }
        Log.v("Delete", "Data is not stable");
    }
}

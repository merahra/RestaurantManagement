package com.apptree.newspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class DisplayItems extends Activity {
    CustomAdapterDisplayMenu adapterDisplayMenu = null;
    private Button btnSubmit;
    private int childCount;
    private CheckBox chkBox;
    private ArrayList<Integer> chkboxState;
    private ArrayList<String> idList;
    private ListView list;
    private MenuPojo mp;
    private ArrayList<MenuPojo> mpList;
    ArrayList<Integer> state;
    private TextView tvId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ONcreate", "INSIDE ON CREATE");
        setContentView(R.layout.display_items);
        this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
        this.list = (ListView) findViewById(R.id.list);
        List<MenuPojo> displayList = Database.fillViewItemsList(this.list);
        if (displayList.isEmpty()) {
            Toast.makeText(this, "No Items To Display In The Menu", 1).show();
            this.btnSubmit.setVisibility(4);
        }
        this.adapterDisplayMenu = (CustomAdapterDisplayMenu) getLastNonConfigurationInstance();
        if (this.adapterDisplayMenu == null) {
            Log.d("-------------------", "inside if");
            this.adapterDisplayMenu = new CustomAdapterDisplayMenu(this, displayList);
        }
        this.list.setAdapter(this.adapterDisplayMenu);
        this.mpList = new ArrayList();
        this.idList = new ArrayList();
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View selectedView, int position, long id) {
                DisplayItems.this.tvId = (TextView) selectedView.findViewById(R.id.tvId);
                DisplayItems.this.chkBox = (CheckBox) selectedView.findViewById(R.id.chkItem);
                DisplayItems.this.chkBox.toggle();
                Log.d("CHECKLIST", String.valueOf(DisplayItems.this.chkBox.isChecked()));
                if (DisplayItems.this.chkBox.isChecked()) {
                    Log.d("CHECKLIST", "++++++++s");
                    DisplayItems.this.mp = (MenuPojo) DisplayItems.this.adapterDisplayMenu.getItem(position);
                    DisplayItems.this.mp.setSelected(true);
                    DisplayItems.this.idList.add(DisplayItems.this.mp.getItemId());
                    DisplayItems.this.adapterDisplayMenu.notifyDataSetChanged();
                    Log.d("Selected", String.valueOf(DisplayItems.this.mp.isSelected()));
                    Log.d("ONCHECK", DisplayItems.this.mp.getItemId());
                }
                if (!DisplayItems.this.chkBox.isChecked()) {
                    Log.d("CHECKLIST", "unchecked");
                    DisplayItems.this.mp = (MenuPojo) DisplayItems.this.adapterDisplayMenu.getItem(position);
                    DisplayItems.this.mp.setSelected(false);
                    DisplayItems.this.idList.remove(DisplayItems.this.mp.getItemId());
                    DisplayItems.this.adapterDisplayMenu.notifyDataSetChanged();
                    Log.d("Selected", String.valueOf(DisplayItems.this.mp.isSelected()));
                    Log.d("CHECKLIST", DisplayItems.this.mp.getItemId());
                }
            }
        });
    }

    public void onSubmit(View v) {
        if (this.idList.size() == 0) {
            Toast.makeText(this, "Please Select an Item", 0).show();
        } else {
            startActivity(new Intent(this, SelectOrderQuantity.class).putStringArrayListExtra("selectedItems", this.idList));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public Object onRetainNonConfigurationInstance() {
        return this.adapterDisplayMenu;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("ONdestroy", "Inside On Destroy");
    }
}

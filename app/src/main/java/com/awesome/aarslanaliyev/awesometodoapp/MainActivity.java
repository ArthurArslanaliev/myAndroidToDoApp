package com.awesome.aarslanaliyev.awesometodoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private FileStorage storage;

    final int DELETE_ACTION_ID = 1;
    final int EDIT_ACTION_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = new FileStorage(getFilesDir(), getString(R.string.file_storage));
        items = storage.readItems();

        ListView listView = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        registerForContextMenu(listView);
    }

    public void onAddItem(View v) {
        EditText newItem = (EditText) findViewById(R.id.newItem);
        String itemText = newItem.getText().toString();
        itemsAdapter.add(itemText);
        newItem.setText("");
        storage.writeItems(items);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ACTION_ID, 0, R.string.context_menu_action_edit);
        menu.add(0, DELETE_ACTION_ID, 1, R.string.context_menu_action_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int actionId = item.getItemId();

        switch (actionId) {
            case DELETE_ACTION_ID:
                items.remove(info.position);
                itemsAdapter.notifyDataSetChanged();
                storage.writeItems(items);
                return true;
            case EDIT_ACTION_ID:
                break;
        }
        return true;
    }
}

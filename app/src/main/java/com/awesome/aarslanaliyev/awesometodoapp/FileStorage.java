package com.awesome.aarslanaliyev.awesometodoapp;

import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileStorage {
    private File filesDir;
    private String fileName;

    public FileStorage(File filesDir, String fileName) {
        this.filesDir = filesDir;
        this.fileName = fileName;
    }

    public void readItems(ListView listView, ArrayAdapter<String> adapter) {
        File storage = new File(this.filesDir, this.fileName);
        try {
            String jsonString = FileUtils.readFileToString(storage);
            Gson gson = new Gson();
            ArrayList deserialize = gson.fromJson(jsonString, ArrayList.class);
            for (int i = 0; i < deserialize.size(); i++) {
                ArrayList item = (ArrayList)deserialize.get(i);
                String text = (String) item.get(0);
                Boolean isChecked = (Boolean) item.get(1);
                adapter.add(text);
                if (isChecked) {
                    listView.setItemChecked(i, true);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public void writeViewItems(ArrayList<String> items, ListView listView) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<Object[]> toSerialize = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (checked.get(i)) {
                toSerialize.add(i, new Object[]{items.get(i), true});
            } else {
                toSerialize.add(i, new Object[]{items.get(i), false});
            }
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(toSerialize);

        File storage = new File(this.filesDir, this.fileName);
        try {
            FileUtils.writeStringToFile(storage, jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.awesome.aarslanaliyev.awesometodoapp;

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

    public ArrayList<String> readItems() {
        File todoFile = new File(this.filesDir, this.fileName);
        ArrayList<String> items;
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void writeItems(ArrayList<String> items) {
        File todoFile = new File(this.filesDir, this.fileName);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

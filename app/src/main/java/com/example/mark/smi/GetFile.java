package com.example.mark.smi;


import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.query.Query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class GetFile {

    public GetFile() {
        ArrayList<String> valores = new ArrayList<String>();
        try {
            Database db = DatabaseBuilder.open(new File("/storage/sdcard0/Download/REAdb.mdb"));
            Table table = db.getTable("Orense");
            for(Column column : table.getColumns()) {
                String columnName = column.getName();
                valores.add(columnName);
            }


        } catch (IOException e) {

        }
    }


}

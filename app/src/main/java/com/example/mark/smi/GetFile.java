package com.example.mark.smi;


import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.query.Query;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class GetFile {

    public GetFile() {
        try {
            Database db = DatabaseBuilder.open(new File("mydb.mdb"));
            Table table = DatabaseBuilder.open(new File("my.mdb")).getTable("MyTable");
            for(Row row : table) {
                System.out.println("Column 'a' has value: " + row.get("a"));
            }
        } catch (IOException e) {

        }
    }
}

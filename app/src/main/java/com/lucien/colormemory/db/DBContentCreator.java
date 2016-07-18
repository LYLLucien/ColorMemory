package com.lucien.colormemory.db;

import android.content.ContentValues;

import com.lucien.colormemory.model.Record;
import com.lucien.colormemory.db.DBHelper.*;

public class DBContentCreator {

    public static ContentValues RecordCreator(Record object) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NAME, object.getUsername());
        values.put(DBConstants.SCORE, object.getScore());
        values.put(DBConstants.CREATE_DATE, object.getCreateTime());
        return values;
    }


}

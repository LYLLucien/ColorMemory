package com.lucien.colormemory.dao;

/**
 * Created by Lucien on 16/7/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.lucien.colormemory.db.DBContentCreator;
import com.lucien.colormemory.db.DBHelper;
import com.lucien.colormemory.db.DBService;
import com.lucien.colormemory.model.Record;

import java.util.List;


public class DBDao implements DBService {

    private final static String CLASSTAG = DBDao.class.getSimpleName();
    private static DBHelper helper;
    private static DBDao dao;
    private SQLiteDatabase database = null;
    private Cursor cursor = null;
    private Context context;


    public static DBDao getDBDaoInstance(Context context) {
        if (dao == null) {
            dao = new DBDao(context);
            Log.i(CLASSTAG, "db is null!");
        } else {
            Log.i(CLASSTAG, "db is not null!");
        }
        return dao;
    }

    private DBDao(Context context) {
        helper = new DBHelper(context);
        this.context = context;
        openDB();
    }

    @Override
    public boolean openDB() {
        Log.i(CLASSTAG, "open DB!");
        try {
            database = helper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean openReadOnlyDB() {
        try {
            database = helper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void closeDB() {
        Log.i(CLASSTAG, "close DB!");
        if (database != null) {
            database.close();
        } else {
            Log.i(CLASSTAG, "Database already close or not open!");
        }
        if (cursor != null) {
            cursor.close();
        } else {
            Log.i(CLASSTAG, "Cursor already close or not open!");
        }
    }

    @Override
    public boolean addItem(String tableName, ContentValues values) {
        boolean flag = false;
        long id;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            id = database.replace(tableName, null, values);
            flag = (id != -1 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteItem(String tableName, String whereClause, String[] whereArgs) {
        boolean flag = false;
        int count = 0;

        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            count = database.delete(tableName, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean updateItem(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        boolean flag = false;
        int count = 0;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            count = database.update(tableName, values, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void bulkInsert(String tableName, List<ContentValues> contentValuesList) {
        long id;
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            database.beginTransaction();
            for (ContentValues values : contentValuesList) {
                id = database.replace(tableName, null, values);
                if (id == -1) {
                    Log.e(CLASSTAG, "bulk insert err, value: " + values.toString());
                }
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
    }

    @Override
    public void bulkUpdate(String tableName, List<ContentValues> contentValuesList, String key, List<Integer> deleteList) {
    }

    @Override
    public Cursor execRawQuery(String sql, String[] whereClause) {
        try {
            if (database == null) {
                database = helper.getWritableDatabase();
            }
            cursor = database.rawQuery(sql, whereClause);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean addRecord(String username, int score) {
        boolean result = false;
        if (!TextUtils.isEmpty(username)) {
            Record record = new Record(username, score, System.currentTimeMillis());
            ContentValues contentValues = DBContentCreator.RecordCreator(record);
            result = addItem(DBHelper.DBConstants.TABLE_RECORD, contentValues);
        }
        return result;
    }

    public Cursor getRank() {
        String query = "SELECT * FROM " + DBHelper.DBConstants.TABLE_RECORD
                + " ORDER BY " + DBHelper.DBConstants.SCORE + " DESC "
                + " LIMIT 5";
        return execRawQuery(query, null);
    }
}

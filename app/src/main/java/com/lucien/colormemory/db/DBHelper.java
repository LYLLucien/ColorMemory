package com.lucien.colormemory.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lucien.li on 2015/10/6.
 * create database class
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String CLASSTAG = DBHelper.class.getSimpleName();
    private Context context;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE IF NOT EXISTS " + DBConstants.TABLE_RECORD + " (" +
                        DBConstants._ID + " " + DBConstants.INTEGER + " PRIMARY KEY AUTOINCREMENT," +
                        DBConstants.NAME + " " + DBConstants.VARCHAR(64) + " UNIQUE, " +
                        DBConstants.SCORE + " " + DBConstants.INTEGER + ", " +
                        DBConstants.CREATE_DATE + " " + DBConstants.INTEGER + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            default:
                db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_RECORD);
                onCreate(db);
        }
    }

    /**
     * db table, field constants
     */
    public static final class DBConstants implements BaseColumns {
        public static final String DB_NAME = "color.db";
        public static final int VERSION = 2;

        public static final String TEXT = "text";
        public static final String INTEGER = "INTEGER";
        public static final String DELETE_ITEM = "D";

        public static String VARCHAR(int num) {
            return "varchar(" + num + ")";
        }

        public static final String TABLE_RECORD = "TblRecord";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String SCORE = "score";
        public static final String CREATE_DATE = "create_date";

    }
}

package com.martin.training.disabledetector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URI;
import java.util.ArrayList;

public class IssuesDB {

    private static final String DB_NAME = "IssuesDB";
    private static final String DB_TABLE = "IssuesTable";
    private static final String KEY_ID = "_id";
    private static final String KEY_LOC = "_loc";
    private static final String KEY_DATE = "_date";
    private static final String KEY_CONDITION = "issue_condition";
    private static final String KEY_IMG = "_img";

    private DatabaseHelper helper;
    private final Context mycontext;
    private SQLiteDatabase database;


    public IssuesDB (Context context){
        mycontext = context;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        public DatabaseHelper(Context context){
            super(context, DB_NAME, null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IssuesTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, _loc TEXT NOT NULL, _date TEXT NOT NULL, issue_condition TEXT NOT NULL, _img TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    public IssuesDB open() throws SQLException {
        helper = new DatabaseHelper(mycontext);
        database = helper.getWritableDatabase();
        return this;
    }
    public void close(){
        helper.close();
    }
    
    public long addInfo(String cond, String loc, String date, String imgUri) throws SQLiteException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONDITION, cond);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_LOC, loc);
        contentValues.put(KEY_IMG, imgUri);

        long result = database.insert(DB_TABLE, null, contentValues);
        return result;
    }
    public ArrayList<String[]> getInfo(){
        String[] columns = new String[]{KEY_ID,KEY_LOC,KEY_DATE,KEY_CONDITION,KEY_IMG};
        Cursor cursor = database.query(DB_TABLE, columns, null, null, null, null, null);

        int indexRowID = cursor.getColumnIndex(KEY_ID);
        int indexLoc = cursor.getColumnIndex(KEY_LOC);
        int indexDate = cursor.getColumnIndex(KEY_DATE);
        int indexCond = cursor.getColumnIndex(KEY_CONDITION);
        int indexImg = cursor.getColumnIndex(KEY_IMG);

        ArrayList<String[]> info = new ArrayList<String[]>();
        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            info.add(new String[]{cursor.getString(indexRowID), cursor.getString(indexLoc), cursor.getString(indexDate), cursor.getString(indexCond), cursor.getString(indexCond)});

        }
        for(int  i = 0; i < info.size(); i++){
            for(int j = 0; j < info.get(i).length; j++){
                System.out.println(info.get(i)[j]);
            }
        }
        return info;
    }
}

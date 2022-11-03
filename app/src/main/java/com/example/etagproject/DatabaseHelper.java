package com.example.etagproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "tag_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "card_bar_code";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " " + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, card_bar_code TEXT NOT NULL)"+";";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+";");
        onCreate(sqLiteDatabase);
    }


    public boolean addData(String itemTagName, String itemBarCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", itemTagName);
        contentValues.put("card_bar_code", itemBarCode);

        System.out.println(TAG + "addData: Adding " + itemTagName + " to " + TABLE_NAME);

//INSERT INTO tag_table (ID,name,card_bar_code)
//VALUES (1, "teszt", "barcode");

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = ("SELECT * FROM " + TABLE_NAME+";");
        Cursor data = sqLiteDatabase.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'"+";";
        Cursor data = sqLiteDatabase.rawQuery(query, null);
        return data;
    }

    public boolean itemExists(String name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT "+ COL2 +" FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'"+";", null);
        Log.d(TAG, "______DATA_____ " + data.moveToFirst());
        return data.moveToFirst();
    }

    public void updateTag(String newName, int id, String oldName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'"+";";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        sqLiteDatabase.execSQL(query);
    }

    public void deleteName(int id, String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        sqLiteDatabase.execSQL(query);
    }

}

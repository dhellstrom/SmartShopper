package com.summer.daniel.smartshopper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.summer.daniel.smartshopper.database.DbSchema.CategoryTable;
import com.summer.daniel.smartshopper.database.DbSchema.ItemTable;
import com.summer.daniel.smartshopper.database.DbSchema.ListTable;
import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;

/**
 * Created by Daniel on 2016-08-10.
 * Helper for the database. Contains the SQL commands for creating the database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + StoreTable.NAME + "(" +
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoreTable.Cols.UUID + ", " +
        StoreTable.Cols.STORE_NAME + ", " +
        StoreTable.Cols.LATITUDE + ", " +
        StoreTable.Cols.LONGITUDE + ", " +
        StoreTable.Cols.CATEGORIES +
        ")");

        db.execSQL("CREATE TABLE " + ItemTable.NAME + "(" +
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        ItemTable.Cols.ITEM_NAME + ", " +
        ItemTable.Cols.CATEGORY +
        ")");

        db.execSQL("CREATE TABLE " + CategoryTable.NAME + "(" +
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CategoryTable.Cols.CATEGORY_NAME +
        ")");

        db.execSQL("CREATE TABLE " + ListTable.NAME + "(" +
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        ListTable.Cols.UUID + ", " +
        ListTable.Cols.LIST_NAME + ", " +
        ListTable.Cols.ITEMS + ", " +
        ListTable.Cols.PURCHASED +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

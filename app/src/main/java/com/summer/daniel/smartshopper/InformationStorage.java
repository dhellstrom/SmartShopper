package com.summer.daniel.smartshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;

/**
 * Created by Daniel on 2016-08-10.
 */
public class InformationStorage {

    private static InformationStorage sInformationStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InformationStorage get(Context context){
        if(sInformationStorage != null){
            sInformationStorage = new InformationStorage(context);
        }
        return sInformationStorage;
    }

    private InformationStorage(Context context){
        mContext = context.getApplicationContext();
    }

    private static ContentValues getContentValues(Store store){
        ContentValues values = new ContentValues();
        values.put(StoreTable.Cols.STORE_NAME, store.getName());
        values.put(StoreTable.Cols.LATITUDE, store.getLocation().latitude);
        values.put(StoreTable.Cols.LONGITUDE, store.getLocation().longitude);
        values.put(StoreTable.Cols.CATEGORIES, transformArrayToString(store.getCategories()));
        return values;
    }

    private static String transformArrayToString(String[] categories){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < categories.length; i++){
            builder.append(categories[i]);
            if(i < categories.length - 1){
                builder.append(";");
            }
        }
        return builder.toString();
    }
}

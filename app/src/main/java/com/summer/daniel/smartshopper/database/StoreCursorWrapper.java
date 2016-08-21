package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.google.android.gms.maps.model.LatLng;
import com.summer.daniel.smartshopper.model.Store;
import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-11.
 * CursorWrapper for easy extraction of stores.
 */
public class StoreCursorWrapper extends CursorWrapper {
    public StoreCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Store getStore(){
        String uuidString = getString(getColumnIndex(StoreTable.Cols.UUID));
        String name = getString(getColumnIndex(StoreTable.Cols.STORE_NAME));
        LatLng location = null;
        int latIndex = getColumnIndex(StoreTable.Cols.LATITUDE);
        int longIndex = getColumnIndex(StoreTable.Cols.LONGITUDE);
        if(!isNull(latIndex) && !isNull(longIndex)){
            double latitude = getDouble(getColumnIndex(StoreTable.Cols.LATITUDE));
            double longitude = getDouble(getColumnIndex(StoreTable.Cols.LONGITUDE));
            location = new LatLng(latitude,longitude);
        }
        String[] categories;
        int categoryIndex = getColumnIndex(StoreTable.Cols.CATEGORIES);
        if(!isNull(categoryIndex)) {
             categories = getString(categoryIndex).split(";");
        }else{
            categories = new String[11];
        }

        return new Store(UUID.fromString(uuidString), name, location, categories);
    }
}

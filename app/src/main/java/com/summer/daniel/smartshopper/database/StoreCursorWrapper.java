package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.google.android.gms.maps.model.LatLng;
import com.summer.daniel.smartshopper.Store;
import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;

/**
 * Created by Daniel on 2016-08-11.
 */
public class StoreCursorWrapper extends CursorWrapper {
    public StoreCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Store getStore(){
        String name = getString(getColumnIndex(StoreTable.Cols.STORE_NAME));
        double latitude = getDouble(getColumnIndex(StoreTable.Cols.LATITUDE));
        double longitude = getDouble(getColumnIndex(StoreTable.Cols.LONGITUDE));
        String[] categories = getString(getColumnIndex(StoreTable.Cols.CATEGORIES)).split(";");

        return new Store(name, new LatLng(latitude, longitude), categories);
    }
}

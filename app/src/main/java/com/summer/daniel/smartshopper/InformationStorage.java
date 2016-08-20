package com.summer.daniel.smartshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.summer.daniel.smartshopper.database.CategoryCursorWrapper;
import com.summer.daniel.smartshopper.database.DatabaseHelper;
import com.summer.daniel.smartshopper.database.DbSchema.CategoryTable;
import com.summer.daniel.smartshopper.database.DbSchema.ItemTable;
import com.summer.daniel.smartshopper.database.DbSchema.ListTable;
import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;
import com.summer.daniel.smartshopper.database.ShopItemCursorWrapper;
import com.summer.daniel.smartshopper.database.ShoppingListCursorWrapper;
import com.summer.daniel.smartshopper.database.StoreCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-10.
 */
public class InformationStorage {

    private static String TAG = "InfoStorage";

    private static InformationStorage sInformationStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InformationStorage get(Context context){
        if(sInformationStorage == null){
            sInformationStorage = new InformationStorage(context);
        }
        return sInformationStorage;
    }

    private InformationStorage(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void addStore(Store store){
        ContentValues values = getContentValues(store);
        mDatabase.insert(StoreTable.NAME, null, values);
    }

    public List<Store> getStores(){
        List<Store> stores = new ArrayList<>();

        StoreCursorWrapper cursor = queryStores(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                stores.add(cursor.getStore());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return stores;
    }

    public Store getStore(UUID id){
        StoreCursorWrapper cursor = queryStores(
                StoreTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getStore();
        }finally {
            cursor.close();
        }
    }

    public void updateStore(Store store){
        String idString = store.getId().toString();
        ContentValues values = getContentValues(store);

        mDatabase.update(StoreTable.NAME, values,
                StoreTable.Cols.UUID + " = ?",
                new String[]{idString});
    }

    public void deleteStore(Store store){
        String idString = store.getId().toString();

        mDatabase.delete(StoreTable.NAME,
                StoreTable.Cols.UUID + " = ?",
                new String[]{idString});
    }

    public void addShopItem(ShopItem item){
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public List<ShopItem> getShopItems(){
        List<ShopItem> items = new ArrayList<>();

        ShopItemCursorWrapper cursor = queryShopItems(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(cursor.getShopItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public ShopItem getShopItem(String itemName) {
        ShopItemCursorWrapper cursor = queryShopItems(
                ItemTable.Cols.ITEM_NAME + " = ?",
                new String[]{itemName}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getShopItem();
        } finally {
            cursor.close();
        }
    }

    public void updateShopItem(ShopItem item){
        String itemName = item.getName();
        ContentValues values = getContentValues(item);
        mDatabase.update(ItemTable.NAME, values,
                ItemTable.Cols.ITEM_NAME + " = ?",
                new String[]{itemName});
    }

    public void addCategory(String category){
        ContentValues values = getContentValues(category);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }

    public List<String> getCategories(){
        List<String> categories = new ArrayList<>();

        CategoryCursorWrapper cursor = queryCategories(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                categories.add(cursor.getCategory());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return categories;
    }


    public void addShoppingList(ShoppingList list){
        ContentValues values = getContentValues(list);
        mDatabase.insert(ListTable.NAME, null, values);
    }

    public List<ShoppingList> getShoppingLists(){
        List<ShoppingList> lists = new ArrayList<>();
        List<ShopItem> items = getShopItems();

        ShoppingListCursorWrapper cursor = queryShoppingLists(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                lists.add(cursor.getShoppingList(items));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return lists;
    }

    public ShoppingList getShoppingList(UUID id) {
        ShoppingListCursorWrapper cursor = queryShoppingLists(
                ListTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getShoppingList(getShopItems());
        } finally {
            cursor.close();
        }
    }

    public void updateShoppingList(ShoppingList list){
        String idString = list.getId().toString();
        ContentValues values = getContentValues(list);

        mDatabase.update(ListTable.NAME, values,
                ListTable.Cols.UUID + " = ?",
                new String[]{idString});
    }

    public void deleteShoppingList(ShoppingList list){
        String idString = list.getId().toString();
        mDatabase.delete(ListTable.NAME,
                ListTable.Cols.UUID + " = ?",
                new String[]{idString});
    }

    private StoreCursorWrapper queryStores(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                StoreTable.NAME,
                null, //selects all coumns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new StoreCursorWrapper(cursor);
    }

    private ShopItemCursorWrapper queryShopItems(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null, //selects all coumns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ShopItemCursorWrapper(cursor);
    }

    private CategoryCursorWrapper queryCategories(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CategoryTable.NAME,
                null, //selects all coumns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CategoryCursorWrapper(cursor);
    }

    private ShoppingListCursorWrapper queryShoppingLists(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ListTable.NAME,
                null, //selects all coumns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ShoppingListCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Store store){
        ContentValues values = new ContentValues();
        values.put(StoreTable.Cols.UUID, store.getId().toString());
        values.put(StoreTable.Cols.STORE_NAME, store.getName());
        LatLng location = store.getLocation();
        if(location != null) {
            values.put(StoreTable.Cols.LATITUDE, location.latitude);
            values.put(StoreTable.Cols.LONGITUDE, location.longitude);
        }
        if(store.getNumberOfCategories() > 0) {
            values.put(StoreTable.Cols.CATEGORIES, transformStringArrayToString(store.getCategories()));
        }else{
            values.putNull(StoreTable.Cols.CATEGORIES);
        }
        return values;
    }

    private static ContentValues getContentValues(ShopItem item){
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.ITEM_NAME, item.getName());
        values.put(ItemTable.Cols.CATEGORY, item.getCategory());
        return values;
    }

    private static ContentValues getContentValues(String category){
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.CATEGORY_NAME, category);
        return values;
    }

    private static ContentValues getContentValues(ShoppingList list){
        ContentValues values = new ContentValues();
        values.put(ListTable.Cols.UUID, list.getId().toString());
        values.put(ListTable.Cols.LIST_NAME, list.getName());
        if(!list.isEmpty()) { //only add these values if the list is not empty
            values.put(ListTable.Cols.ITEMS, transformStringArrayToString(list.getItemNames()));
            values.put(ListTable.Cols.PURCHASED, transformBooleanListToString(list.getPurchased()));
        }else{ //put null if list is empty
            values.putNull(ListTable.Cols.ITEMS);
            values.putNull(ListTable.Cols.PURCHASED);
        }
        return values;
    }

    private static String transformStringArrayToString(String[] array){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            builder.append(array[i]);
            if(i < array.length - 1){
                builder.append(";");
            }
        }
        return builder.toString();
    }

    private static String transformBooleanListToString(List<Boolean> list){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            builder.append(list.get(i));
            if(i < list.size() - 1){
                builder.append(";");
            }
        }
        return builder.toString();
    }
}

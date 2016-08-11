package com.summer.daniel.smartshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.summer.daniel.smartshopper.database.DatabaseHelper;
import com.summer.daniel.smartshopper.database.DbSchema.CategoryTable;
import com.summer.daniel.smartshopper.database.DbSchema.ItemTable;
import com.summer.daniel.smartshopper.database.DbSchema.ListTable;
import com.summer.daniel.smartshopper.database.DbSchema.StoreTable;

import java.util.List;

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
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void addStore(Store store){
        ContentValues values = getContentValues(store);
        mDatabase.insert(StoreTable.NAME, null, values);
    }

    public void updateStore(Store store){
        String name = store.getName();
        ContentValues values = getContentValues(store);

        mDatabase.update(StoreTable.NAME, values,
                StoreTable.Cols.STORE_NAME + " = ?",
                new String[]{name});
    }

    public void addShopItem(ShopItem item){
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void addCategory(String category){
        ContentValues values = getContentValues(category);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }

    public void addShoppingList(ShoppingList list){
        ContentValues values = getContentValues(list);
        mDatabase.insert(ListTable.NAME, null, values);
    }

    public void updateShoppingList(ShoppingList list){
        String idString = list.getId().toString();
        ContentValues values = getContentValues(list);

        mDatabase.update(ListTable.NAME, values,
                ListTable.Cols.UUID + " = ?",
                new String[]{idString});
    }

    private static ContentValues getContentValues(Store store){
        ContentValues values = new ContentValues();
        values.put(StoreTable.Cols.STORE_NAME, store.getName());
        values.put(StoreTable.Cols.LATITUDE, store.getLocation().latitude);
        values.put(StoreTable.Cols.LONGITUDE, store.getLocation().longitude);
        values.put(StoreTable.Cols.CATEGORIES, transformStringArrayToString(store.getCategories()));
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
        values.put(ListTable.Cols.ITEMS, transformStringArrayToString(list.getItemNames()));
        values.put(ListTable.Cols.PURCHASED, transformBooleanListToString(list.getPurchased()));
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

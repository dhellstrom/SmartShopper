package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.summer.daniel.smartshopper.ShopItem;
import com.summer.daniel.smartshopper.database.DbSchema.ItemTable;

/**
 * Created by Daniel on 2016-08-11.
 */
public class ShopItemCursorWrapper extends CursorWrapper {
    public ShopItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ShopItem getShopItem(){
        String name = getString(getColumnIndex(ItemTable.Cols.ITEM_NAME));
        String category = getString(getColumnIndex(ItemTable.Cols.CATEGORY));

        return new ShopItem(name, category);
    }
}

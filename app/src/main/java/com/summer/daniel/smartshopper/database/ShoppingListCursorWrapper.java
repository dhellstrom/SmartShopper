package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.summer.daniel.smartshopper.ShopItem;
import com.summer.daniel.smartshopper.ShoppingList;
import com.summer.daniel.smartshopper.database.DbSchema.ListTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-11.
 */
public class ShoppingListCursorWrapper extends CursorWrapper {
    public ShoppingListCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ShoppingList getShoppingList(List<ShopItem> savedItems){
        String idString = getString(getColumnIndex(ListTable.Cols.UUID));
        String name = getString(getColumnIndex(ListTable.Cols.LIST_NAME));
        String[] itemsArray = getString(getColumnIndex(ListTable.Cols.ITEMS)).split(";");
        String[] purchasedArray = getString(getColumnIndex(ListTable.Cols.PURCHASED)).split(";");

        //Uses list of saved items to give the correct category to items
        List<ShopItem> items = new ArrayList<>();
        for(int i = 0; i < itemsArray.length; i++){
            items.add(new ShopItem(itemsArray[i], savedItems.get(savedItems.indexOf(itemsArray[i]))
                    .getCategory()));
        }

        List<Boolean> purchased = new ArrayList<>();
        for(int i = 0; i < purchasedArray.length; i++){
            String b = purchasedArray[i];
            if(b.equals(true)){
                purchased.add(true);
            }else{
                purchased.add(false);
            }
        }

        return new ShoppingList(UUID.fromString(idString), name, items, purchased);
    }
}

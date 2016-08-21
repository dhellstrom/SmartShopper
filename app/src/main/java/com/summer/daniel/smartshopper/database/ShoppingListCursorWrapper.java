package com.summer.daniel.smartshopper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.summer.daniel.smartshopper.model.ShopItem;
import com.summer.daniel.smartshopper.model.ShoppingList;
import com.summer.daniel.smartshopper.database.DbSchema.ListTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-11.
 * CursorWrapper for easy extraction of shoppingLists.
 */
public class ShoppingListCursorWrapper extends CursorWrapper {
    public ShoppingListCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ShoppingList getShoppingList(List<ShopItem> savedItems){
        String idString = getString(getColumnIndex(ListTable.Cols.UUID));
        String name = getString(getColumnIndex(ListTable.Cols.LIST_NAME));

        List<ShopItem> items = new ArrayList<>();
        List <Boolean> purchased = new ArrayList<>();

        int itemsIndex = getColumnIndex(ListTable.Cols.ITEMS);
        if(!isNull(itemsIndex)){
            //Uses list of saved items to give the correct category to items
            String[] itemsArray = getString(itemsIndex).split(";");
            for(int i = 0; i < itemsArray.length; i++){
                items.add(new ShopItem(itemsArray[i], savedItems.get(savedItems
                        .indexOf(new ShopItem(itemsArray[i], "")))
                        .getCategory()));
            }
        }

        int purchasedIndex = getColumnIndex(ListTable.Cols.PURCHASED);
        if(!isNull(purchasedIndex)) {
            String[] purchasedArray = getString(purchasedIndex).split(";");
            for (int i = 0; i < purchasedArray.length; i++) {
                String b = purchasedArray[i];
                if (b.equals("true")) {
                    purchased.add(true);
                } else {
                    purchased.add(false);
                }
            }
        }

        return new ShoppingList(UUID.fromString(idString), name, items, purchased);
    }
}

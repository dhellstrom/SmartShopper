package com.summer.daniel.smartshopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-11.
 */
public class ShoppingList {

    private UUID mId;
    private String mName;
    private List<ShopItem> mItems;
    private List<Boolean> mPurchased;

    /**
     * Creates a shopping list with a chosen id and name. Used when loading from database.
     * @param id List id
     * @param name List name
     * @param items List of items
     * @param purchased List containing purchased status of items in list
     */
    public ShoppingList(UUID id, String name, List<ShopItem> items, List<Boolean> purchased){
        mId = id;
        mName = name;
        mItems = items;
        mPurchased = purchased;
    }

    /**
     * Creates a shopping list with a chosen name and random id. Used when creating a new list.
     * @param name List name
     */
    public ShoppingList(String name){
        this(UUID.randomUUID(), name, new ArrayList<ShopItem>(), new ArrayList<Boolean>());
    }

    /**
     * Creates a shopping list with random id and a default name. Used when creating a new list.
     */
    public ShoppingList(){
        this(UUID.randomUUID(), "Shopping List", new ArrayList<ShopItem>(), new ArrayList<Boolean>());
    }

    public void addItem(ShopItem item){
        mItems.add(item);
        mPurchased.add(false);
    }

    public void removeItem(ShopItem item){
        int index = mItems.indexOf(item);
        mItems.remove(index);
        mPurchased.remove(index);
    }

    public UUID getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    public String[] getItemNames(){
        String[] result = new String[mItems.size()];
        for(int i = 0; i < mItems.size(); i++){
            result[i] = mItems.get(i).getName();
        }
        return result;
    }

    public List<Boolean> getPurchased(){
        return mPurchased;
    }
}

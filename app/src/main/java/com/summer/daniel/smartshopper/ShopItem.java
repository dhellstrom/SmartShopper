package com.summer.daniel.smartshopper;

/**
 * Created by Daniel on 2016-08-10.
 */
public class ShopItem {

    private String mName;
    private String mCategory;

    public ShopItem(String name, String category){
        mName = name;
        mCategory = category;
    }

    public String getName(){
        return mName;
    }

    public String getCategory(){
        return mCategory;
    }
}

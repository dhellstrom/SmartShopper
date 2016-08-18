package com.summer.daniel.smartshopper;

/**
 * Created by Daniel on 2016-08-10.
 */
public class ShopItem {

    public static final String NO_CATEGORY = "No category";

    private String mName;
    private String mCategory;

    public ShopItem(String name, String category){
        mName = name;
        mCategory = category;
    }

    public void setName(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    }

    public void setCategory(String category){
        mCategory = category;
    }

    public String getCategory(){
        return mCategory;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ShopItem){
            return ((ShopItem) o).getName().equals(mName);
        }
        return false;
    }
}

package com.summer.daniel.smartshopper;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Daniel on 2016-08-10.
 */
public class Store {

    private String mName;
    private LatLng mLocation;
    private String[] mCategories;
    private int mNumberOfCategories;

    public Store(String name, LatLng location, String[] categories){
        mName = name;
        mLocation = location;
        mCategories = categories;
    }

    public Store(String name, LatLng location){
        this(name, location, new String[11]);
    }

    public String getName(){
        return mName;
    }

    public LatLng getLocation(){
        return mLocation;
    }

    public String[] getCategories(){
        String[] result = new String[mNumberOfCategories];
        for(int i = 0; i < mNumberOfCategories; i++){
            result[i] = mCategories[i];
        }
        return result;
    }

    public void addCategory(String category){
        int i = 0;
        while(i < mCategories.length){
            if(mCategories[i] == null){
                mCategories[i] = category;
                break;
            }
            i++;
        }
        if(i == mCategories.length){
            String[] temp = mCategories;
            mCategories = new String[mCategories.length*2];
            for(int j = 0; j < temp.length; j++){
                mCategories[j] = temp[j];
            }
            mCategories[i] = category;
        }
        mNumberOfCategories++;
    }

    public int getNumberOfCategories(){
        return mNumberOfCategories;
    }

    public void increasePriority(int pos){
        String temp = mCategories[pos];
        mCategories[pos] = mCategories[pos-1];
        mCategories[pos-1] = temp;
    }

    public void decreasePriority(int pos){
        String temp = mCategories[pos];
        mCategories[pos] = mCategories[pos+1];
        mCategories[pos+1] = temp;
    }
}

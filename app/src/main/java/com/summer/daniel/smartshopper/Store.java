package com.summer.daniel.smartshopper;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-10.
 */
public class Store {

    private UUID mId;
    private String mName;
    private LatLng mLocation;
    private String[] mCategories;
    private int mNumberOfCategories;

    public Store(UUID id, String name, LatLng location, String[] categories){
        mId = id;
        mName = name;
        mLocation = location;
        mCategories = categories;
        int i = 0;
        while(i < mCategories.length && mCategories[i] != null){
            i++;
        }
        mNumberOfCategories = i;
    }

    public Store(String name, LatLng location){
        this(UUID.randomUUID(), name, location, new String[11]);
    }

    public UUID getId(){
        return mId;
    }
    public void setName(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    }

    public void setLocation(LatLng location){
        mLocation = location;
    }

    public LatLng getLocation(){
        return mLocation;
    }

    public boolean hasLocation(){
        return mLocation != null;
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

    public void increasePriority(String category){
        int pos = -1;
        for(int i = 0; i < mNumberOfCategories; i++){
            if(mCategories[i].equals(category)){
                pos = i;
                break;
            }
        }
        if(pos > 0) {
            String temp = mCategories[pos];
            mCategories[pos] = mCategories[pos - 1];
            mCategories[pos - 1] = temp;
        }
    }

    public void decreasePriority(String category){
        int pos = -1;
        for(int i = 0; i < mNumberOfCategories; i++){
            if(mCategories[i].equals(category)){
                pos = i;
                break;
            }
        }
        if(pos > -1 && pos < mNumberOfCategories - 1) {
            String temp = mCategories[pos];
            mCategories[pos] = mCategories[pos + 1];
            mCategories[pos + 1] = temp;
        }
    }
}

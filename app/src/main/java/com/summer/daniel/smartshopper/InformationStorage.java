package com.summer.daniel.smartshopper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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


    }
}

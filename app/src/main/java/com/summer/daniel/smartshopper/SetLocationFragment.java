package com.summer.daniel.smartshopper;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;


/**
 * Created by Daniel on 2016-08-19.
 */
public class SetLocationFragment extends SupportMapFragment {

    private static final String ARG_STORE_NAME = "com.summer.daniel.smartshopper.setLocationFragment.storeName";

    public static SetLocationFragment newInstance(String storeName){
        Bundle args = new Bundle();
        args.putString(ARG_STORE_NAME, storeName);

        SetLocationFragment fragment = new SetLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }


}

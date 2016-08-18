package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Daniel on 2016-08-18.
 */
public class EditStoreFragment extends Fragment {

    private static final String ARGS_STORE_NAME = "com.summer.daniel.smartshopper.editStoreFragment.storeName";

    public static Fragment newInstance(String storeName){
        Bundle args = new Bundle();
        args.putString(ARGS_STORE_NAME, storeName);

        EditStoreFragment fragment = new EditStoreFragment();
        fragment.setArguments(args);
        return fragment;

    }
}

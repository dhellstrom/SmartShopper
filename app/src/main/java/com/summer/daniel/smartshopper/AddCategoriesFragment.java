package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-20.
 */
public class AddCategoriesFragment extends Fragment {

    private static final String ARG_STORE_ID = "com.summer.daniel.smartshopper.addCategoriesFragment.storeId";

    private Store mStore;

    public static AddCategoriesFragment newInstance(UUID storeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE_ID, storeId);

        AddCategoriesFragment fragment = new AddCategoriesFragment();
        fragment.setArguments(args);
        return fragment;

    }


}

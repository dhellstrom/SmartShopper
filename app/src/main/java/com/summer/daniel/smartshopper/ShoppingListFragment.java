package com.summer.daniel.smartshopper;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class ShoppingListFragment extends Fragment {

    public static Fragment newInstance(UUID listId){
        return new ShoppingListFragment();
    }
}

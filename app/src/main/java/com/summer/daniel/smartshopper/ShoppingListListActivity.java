package com.summer.daniel.smartshopper;

import android.support.v4.app.Fragment;

public class ShoppingListListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new ShoppingListListFragment();
    }
}

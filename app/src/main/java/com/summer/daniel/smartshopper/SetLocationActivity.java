package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SetLocationActivity extends SingleFragmentActivity {

    private static final String EXTRA_STORE_NAME = "com.summer.daniel.smartshopper.setLocationActivity.storeName";

    public static Intent newIntent(Context context, String storeName){
        Intent intent = new Intent(context, SetLocationActivity.class);
        intent.putExtra(EXTRA_STORE_NAME, storeName);
        return intent;
    }

    protected Fragment createFragment(){
        return SetLocationFragment.newInstance(getIntent().getStringExtra(EXTRA_STORE_NAME));
    }


}

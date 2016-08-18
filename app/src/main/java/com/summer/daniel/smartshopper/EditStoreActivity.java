package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditStoreActivity extends SingleFragmentActivity {

    private static final String EXTRA_STORE_NAME = "com.summer.daniel.smartshopper.editStoreActivity.storeName";


    @Override
    protected Fragment createFragment() {
        return EditStoreFragment.newInstance(getIntent().getStringExtra(EXTRA_STORE_NAME));
    }

    public static Intent newIntent(Context context,String storeName){
        Intent intent = new Intent(context, EditStoreActivity.class);
        intent.putExtra(EXTRA_STORE_NAME, storeName);
        return intent;
    }
}

package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class EditStoreActivity extends SingleFragmentActivity {

    private static final String EXTRA_STORE_ID = "com.summer.daniel.smartshopper.editStoreActivity.storeId";


    @Override
    protected Fragment createFragment() {
        return EditStoreFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_STORE_ID));
    }

    public static Intent newIntent(Context context, UUID storeId){
        Intent intent = new Intent(context, EditStoreActivity.class);
        intent.putExtra(EXTRA_STORE_ID, storeId);
        return intent;
    }
}

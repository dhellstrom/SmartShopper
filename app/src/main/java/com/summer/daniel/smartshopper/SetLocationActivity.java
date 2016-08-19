package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class SetLocationActivity extends SingleFragmentActivity {

    private static final String EXTRA_STORE_ID = "com.summer.daniel.smartshopper.setLocationActivity.storeId";

    public static Intent newIntent(Context context, UUID storeId){
        Intent intent = new Intent(context, SetLocationActivity.class);
        intent.putExtra(EXTRA_STORE_ID, storeId);
        return intent;
    }

    protected Fragment createFragment(){
        return SetLocationFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_STORE_ID));
    }


}

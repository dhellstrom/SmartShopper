package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.StringTokenizer;
import java.util.UUID;

public class AddCategoriesActivity extends SingleFragmentActivity {

    private static final String EXTRA_STORE_ID = "com.summer.daniel.smartshopper.addCategoriesActivity.storeId";

    @Override
    protected Fragment createFragment() {
        return AddCategoriesFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_STORE_ID));
    }

    public static Intent newIntent(Context context, UUID storeId){
        Intent intent = new Intent(context, AddCategoriesActivity.class);
        intent.putExtra(EXTRA_STORE_ID, storeId);
        return intent;
    }
}

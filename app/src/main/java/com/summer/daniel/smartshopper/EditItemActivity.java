package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditItemActivity extends SingleFragmentActivity {

    private static final String EXTRA_ITEM_NAME = "com.summer.daniel.smartshopper.editItemActivity.itemName";

    @Override
    protected Fragment createFragment() {
        return EditItemFragment.newInstance(getIntent().getStringExtra(EXTRA_ITEM_NAME));
    }

    public static Intent getIntent(Context context, String itemName){
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra(EXTRA_ITEM_NAME, itemName);
        return intent;
    }
}

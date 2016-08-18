package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ShoppingListActivity extends SingleFragmentActivity {

    private static final String EXTRA_LIST_ID = "com.summer.daniel.smartshopper.shoppingListActivity.list_id";

    @Override
    protected Fragment createFragment() {
        return ShoppingListFragment.newInstance(
                (UUID) getIntent().getSerializableExtra(EXTRA_LIST_ID));
    }

    public static Intent newIntent(Context context, UUID listId){
        Intent intent = new Intent(context, ShoppingListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listId);

        return intent;
    }


}

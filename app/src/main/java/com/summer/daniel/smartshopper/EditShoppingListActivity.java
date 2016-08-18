package com.summer.daniel.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class EditShoppingListActivity extends SingleFragmentActivity {

    private static final String EXTRA_LIST_ID = "com.summer.daniel.smartshopper.editShoppingListActivity.list_id";

    protected Fragment createFragment(){
        return EditShoppingListFragment.newInstance(
                (UUID) getIntent().getSerializableExtra(EXTRA_LIST_ID));
    }

    public static Intent newIntent(Context context, UUID listId){
        Intent intent = new Intent(context, EditShoppingListActivity.class);
        intent.putExtra(EXTRA_LIST_ID, listId);
        return intent;
    }
}

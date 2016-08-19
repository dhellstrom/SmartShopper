package com.summer.daniel.smartshopper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class ShoppingListListActivity extends SingleFragmentActivity {

    private static final int REQUEST_ERROR = 0;


    @Override
    protected Fragment createFragment() {
        return new ShoppingListListFragment();
    }

    @Override
    protected void onResume(){
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //can't run without play services, so exits app
                            finish();
                        }
                    });
            errorDialog.show();
        }

    }
}

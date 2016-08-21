package com.summer.daniel.smartshopper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Daniel on 2016-08-20.
 * Dialog that the user inputs the name of the category that should be created.
 */
public class NewCategoryFragment extends DialogFragment {

    public static final String EXTRA_CATEGORY_NAME =
            "com.summer.daniel.smartshopper.newCategoryFragment.categoryName";

    private EditText mCategoryField;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_new_category, null);

        mCategoryField = (EditText) v.findViewById(R.id.dialog_category_name);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.new_category_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = mCategoryField.getText().toString();
                        sendResult(Activity.RESULT_OK, category);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, String category){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY_NAME, category);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

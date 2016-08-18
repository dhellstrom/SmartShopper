package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Daniel on 2016-08-18.
 */
public class EditItemFragment extends Fragment {

    private static final String ARG_ITEM_NAME = "com.summer.daniel.smartshopper.editItemFragment.itemName";

    private EditText mNameField;
    private EditText mCategoryField;

    private ShopItem mItem;

    public static Fragment newInstance(String itemName){
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, itemName);

        Fragment fragment = new EditItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String itemName = getArguments().getString(ARG_ITEM_NAME);
        mItem = InformationStorage.get(getActivity()).getShopItem(itemName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_edit_item, parent, false);

        mNameField = (EditText) v.findViewById(R.id.edit_item_name_field);
        mNameField.setText(mItem.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
        });

        mCategoryField = (EditText) v.findViewById(R.id.edit_item_category_field);
        mCategoryField.setText(mItem.getCategory());
        mCategoryField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setCategory(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
        });

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();

        InformationStorage storage = InformationStorage.get(getActivity());
        storage.updateShopItem(mItem);

        //only add as new category if relevant
        List<String> categories = storage.getCategories();
        if(!categories.contains(mItem.getCategory()) && !mItem.getCategory().equals(ShopItem.NO_CATEGORY)){
            storage.addCategory(mItem.getCategory());
        }
    }
}

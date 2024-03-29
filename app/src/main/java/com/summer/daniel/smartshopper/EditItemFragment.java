package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.summer.daniel.smartshopper.model.InformationStorage;
import com.summer.daniel.smartshopper.model.ShopItem;

import java.util.List;

/**
 * Created by Daniel on 2016-08-18.
 * Fragment used to create a new item nad decide its category.
 */
public class EditItemFragment extends Fragment {

    private static final String ARG_ITEM_NAME = "com.summer.daniel.smartshopper.editItemFragment.itemName";

    private EditText mNameField; //disabled. Enable to support editing of items after they have been added.
    private EditText mCategoryField;

    private ShopItem mItem;

    public static EditItemFragment newInstance(String itemName){
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, itemName);

        EditItemFragment fragment = new EditItemFragment();
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

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(R.string.edit_item_title);

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();

        InformationStorage storage = InformationStorage.get(getActivity());
        storage.updateShopItem(mItem);

        //only add as new category if not already in database
        List<String> categories = storage.getCategories();
        if(!categories.contains(mItem.getCategory()) && !mItem.getCategory().equals(ShopItem.NO_CATEGORY)){
            storage.addCategory(mItem.getCategory());
        }
    }
}

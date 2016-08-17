package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class EditShoppingListFragment extends Fragment {

    private static final String ARGS_LIST_ID = "com.summer.daniel.smartshopper.editShoppingListFragment.list_id";

    private EditText mListName;
    private RecyclerView mListContents;

    private ShoppingList mList;

    public static Fragment newInstance(UUID listId){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_LIST_ID, listId);

        EditShoppingListFragment fragment = new EditShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        InformationStorage storage = InformationStorage.get(getActivity());

        UUID listId = (UUID) getArguments().getSerializable(ARGS_LIST_ID);
        if(listId == null){
            mList = new ShoppingList();
            storage.addShoppingList(mList); //adds list on creation. Maybe change to avoid too many lists
        }else{
            mList = storage.getShoppingList(listId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_edit_shopping_list, parent, false);

        mListName = (EditText) v.findViewById(R.id.edit_list_name_edit_text);
        mListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
        });

        mListContents = (RecyclerView) v.findViewById(R.id.edit_list_recycler_view);

        updateUI();

        return v;

    }

    @Override
    public void onPause(){
        super.onPause();

        InformationStorage.get(getActivity()).updateShoppingList(mList);
    }

    private void updateUI(){
        mListName.setText(mList.getName());
    }
}

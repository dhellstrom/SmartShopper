package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class EditShoppingListFragment extends Fragment {

    private static final String ARGS_LIST_ID = "com.summer.daniel.smartshopper.editShoppingListFragment.list_id";
    private static final String KEY_LIST_ID = "id";
    private static final String CONST_CREATE_ITEM = "Create new item";

    private EditText mListName;
    private RecyclerView mListContents;
    private SearchView mSearchView;

    private ShopItemAdapter mItemAdapter;
    private SimpleCursorAdapter mSearchAdapter;

    private ShoppingList mList;

    public static EditShoppingListFragment newInstance(UUID listId){
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

        UUID listId;
        if(savedInstanceState != null){
            listId = (UUID) savedInstanceState.getSerializable(KEY_LIST_ID);
        }else {
            listId = (UUID) getArguments().getSerializable(ARGS_LIST_ID);
        }
        if(listId == null){
            mList = new ShoppingList();
            storage.addShoppingList(mList);
        }else{
            mList = storage.getShoppingList(listId);
        }

        mSearchAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,
                null, new String[]{"itemName"}, new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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
        mListContents.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchView = (SearchView) v.findViewById(R.id.edit_list_search_view);
        mSearchView.setSuggestionsAdapter(mSearchAdapter);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getActivity().getResources().getString(R.string.shopping_list_search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                //do nothing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //update the adapter so suggestions are shown
                updateSearchAdapter(newText);
                return false;
            }
        });
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener(){

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) mSearchAdapter.getItem(position);
                String itemName;
                try{
                    cursor.moveToPosition(position);
                    itemName = cursor.getString(cursor.getColumnIndex("itemName"));
                }finally {
                    cursor.close();
                }
                if(itemName.equals(CONST_CREATE_ITEM)){
                    //user clicked on "Create new item" so starts a new intent for creating a new item
                    // with the text in the searchView as name
                    ShopItem newItem = new ShopItem(mSearchView.getQuery().toString(), ShopItem.NO_CATEGORY);
                    InformationStorage.get(getActivity()).addShopItem(newItem);
                    mList.addItem(newItem);
                    Intent intent = EditItemActivity.newIntent(getActivity(), newItem.getName());
                    startActivity(intent);
                }else{
                    mList.addItem(InformationStorage.get(getActivity()).getShopItem(itemName));
                    updateUI();
                }

                mSearchView.setQuery("", false);
                return true;
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(R.string.edit_list_title);

        updateUI();

        return v;

    }

    @Override
    public void onPause(){
        super.onPause();

        InformationStorage.get(getActivity()).updateShoppingList(mList);
    }

    @Override
    public void onResume(){
        super.onResume();

        mList = InformationStorage.get(getActivity()).getShoppingList(mList.getId());
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_LIST_ID, mList.getId());
    }

    private void updateUI(){
        mListName.setText(mList.getName());

        List<ShopItem> items = mList.getItems();
        if(mItemAdapter == null){
            mItemAdapter = new ShopItemAdapter(items);
            mListContents.setAdapter(mItemAdapter);
        }else{
            mItemAdapter.setItems(items);
            mItemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates the searchAdapter by adding all items starting with the text
     * that has been typed into the searchView.
     *
     */
    private void updateSearchAdapter(String query){
        List<ShopItem> items = InformationStorage.get(getActivity()).getShopItems();

        MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "itemName"});
        for(int i = 0; i < items.size(); i++){
            String itemName = items.get(i).getName();
            if(itemName.toLowerCase().startsWith(query.toLowerCase())){
                cursor.addRow(new Object[]{i, itemName});
            }
        }
        //if there are no matching items saved, add "Create new item", making the user
        //able to create the item and add it to the list of saved items
        if(cursor.getCount() == 0){
            cursor.addRow(new Object[]{0, CONST_CREATE_ITEM});
        }
        mSearchAdapter.changeCursor(cursor);
    }

    private class ShopItemHolder extends RecyclerView.ViewHolder{

        private TextView mNameTextView;
        private TextView mCategoryTextView;
        private Button mDeleteButton;

        private ShopItem mItem;

        public ShopItemHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.edit_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.edit_list_item_category);
            mDeleteButton = (Button) itemView.findViewById(R.id.edit_list_item_delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mList.removeItem(mItem.getName());
                    updateUI();
                }
            });
        }

        public void bindItem(ShopItem item){
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mCategoryTextView.setText(mItem.getCategory());
        }
    }

    private class ShopItemAdapter extends RecyclerView.Adapter<ShopItemHolder>{

        private List<ShopItem> mItems;

        public ShopItemAdapter(List<ShopItem> items){
            mItems = items;
        }

        @Override
        public ShopItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_edit_list, parent, false);

            return new ShopItemHolder(v);
        }

        @Override
        public void onBindViewHolder(ShopItemHolder holder, int position){
            ShopItem item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount(){
            return mItems.size();
        }

        public void setItems(List<ShopItem> items){
            mItems = items;
        }
    }
}

package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class ShoppingListFragment extends Fragment {


    private static final String ARGS_LIST_ID = "com.summer.daniel.smartshopper.shoppingListFragment.list_id";

    private TextView mListName;
    private RecyclerView mListContents;
    private ShopItemAdapter mAdapter;

    private ShoppingList mList;

    public static Fragment newInstance(UUID listId){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_LIST_ID, listId);

        Fragment fragment = new ShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mList = InformationStorage.get(getActivity())
                .getShoppingList((UUID) getArguments().getSerializable(ARGS_LIST_ID));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_shopping_list, parent, false);

        mListName = (TextView) v.findViewById(R.id.shopping_list_name);
        mListName.setText(mList.getName());

        mListContents = (RecyclerView) v.findViewById(R.id.shopping_list_recycler_view);
        mListContents.setLayoutManager(new LinearLayoutManager(getActivity()));

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_shopping_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_edit_list:
                Intent intent = EditShoppingListActivity.getIntent(getActivity(), mList.getId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI(){
        mListName.setText(mList.getName());

        List<ShopItem> items = mList.getItems();

        if(mAdapter == null){
            mAdapter = new ShopItemAdapter(items);
            mListContents.setAdapter(mAdapter);
        }else{
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }



    //troligtvis ta ut dessa till en egen klass
    private class ShopItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;
        private TextView mCategoryTextView;
        private CheckBox mPurchasedCheckBox;

        private ShopItem mItem;

        public ShopItemHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.shopping_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.shopping_list_item_category);
            mPurchasedCheckBox = (CheckBox) itemView.findViewById(R.id.shopping_list_item_purchased);

            itemView.setOnClickListener(this);
        }

        public void bindItem(ShopItem item){
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mCategoryTextView.setText(mItem.getCategory());
            mPurchasedCheckBox.setChecked(mList.getItemPurchasedStatus(mItem.getName()));
        }

        @Override
        public void onClick(View view){
            mList.toggleItemPurchased(mItem.getName());
            mPurchasedCheckBox.setChecked(mList.getItemPurchasedStatus(mItem.getName()));
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
            View v = inflater.inflate(R.layout.item_shopping_list, parent, false); //ändra här

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

package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summer.daniel.smartshopper.model.InformationStorage;
import com.summer.daniel.smartshopper.model.ShoppingList;

import java.util.List;

/**
 * Created by Daniel on 2016-08-18.
 * Launcher fragment. Displays a list of all saved shopping lists.
 */
public class ShoppingListListFragment extends AbstractListFragment {

    private ShoppingListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_main_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_edit_stores:
                Intent intent = new Intent(getActivity(), StoreListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void updateUI(RecyclerView recyclerView){
        InformationStorage storage = InformationStorage.get(getActivity());
        List<ShoppingList> lists = storage.getShoppingLists();

        if(mAdapter == null){
            mAdapter = new ShoppingListAdapter(lists);
            recyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setLists(lists);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected Intent newObjectIntent(){
        return EditShoppingListActivity.newIntent(getActivity(), null);
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;

        private ShoppingList mList;

        public ShoppingListHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.main_list_object_name);

            itemView.setOnClickListener(this);
        }

        public void bindList(ShoppingList list){
            mList = list;
            mNameTextView.setText(mList.getName());
        }

        @Override
        public void onClick(View view){
            Intent intent = ShoppingListActivity.newIntent(getActivity(), mList.getId());
            startActivity(intent);
        }
    }

    private class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListHolder>{

        private List<ShoppingList> mLists;

        public ShoppingListAdapter(List<ShoppingList> lists){
            mLists = lists;
        }

        @Override
        public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_main_list, parent, false);

            return new ShoppingListHolder(v);
        }

        @Override
        public void onBindViewHolder(ShoppingListHolder holder, int position){
            ShoppingList list = mLists.get(position);
            holder.bindList(list);
        }

        @Override
        public int getItemCount(){
            return mLists.size();
        }

        public void setLists(List<ShoppingList> lists){
            mLists = lists;
        }
    }
}

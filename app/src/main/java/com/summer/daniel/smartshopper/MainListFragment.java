package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 2016-08-10.
 */
public class MainListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewListFab;

    private ShoppingListAdapter mAdapter;

    public static Fragment newInstance(){
        return new MainListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_main_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.main_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewListFab = (FloatingActionButton) v.findViewById(R.id.fab_new_list);
        mNewListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditShoppingListActivity.getIntent(getActivity(), null);
                startActivity(intent);
            }
        });

        updateUI();
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI();
    }

    private void updateUI(){
        InformationStorage storage = InformationStorage.get(getActivity());
        List<ShoppingList> lists = storage.getShoppingLists();

        if(mAdapter == null){
            mAdapter = new ShoppingListAdapter(lists);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setLists(lists);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_list, menu);
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;

        private ShoppingList mList;

        public ShoppingListHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.main_list_list_name);

            itemView.setOnClickListener(this);
        }

        public void bindList(ShoppingList list){
            mNameTextView.setText(list.getName());
        }

        @Override
        public void onClick(View view){
            //start new ShoppingListActivity
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

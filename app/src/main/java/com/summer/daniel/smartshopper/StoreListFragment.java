package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summer.daniel.smartshopper.model.InformationStorage;
import com.summer.daniel.smartshopper.model.Store;

import java.util.List;

/**
 * Created by Daniel on 2016-08-18.
 * Fragment used to display all saved stores.
 */
public class StoreListFragment extends AbstractListFragment {


    private StoreAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(R.string.store_list_title);

        return v;
    }

    protected void updateUI(RecyclerView recyclerView){
        InformationStorage storage = InformationStorage.get(getActivity());
        List<Store> stores = storage.getStores();

        if(mAdapter == null){
            mAdapter = new StoreAdapter(stores);
            recyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setStores(stores);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected Intent newObjectIntent(){
        return EditStoreActivity.newIntent(getActivity(), null);
    }

    private class StoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;

        private Store mStore;

        public StoreHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.main_list_object_name);

            itemView.setOnClickListener(this);
        }

        public void bindList(Store store){
            mStore = store;
            mNameTextView.setText(mStore.getName());
        }

        @Override
        public void onClick(View view){
            Intent intent = EditStoreActivity.newIntent(getActivity(), mStore.getId());
            startActivity(intent);
        }
    }

    private class StoreAdapter extends RecyclerView.Adapter<StoreHolder>{

        private List<Store> mStores;

        public StoreAdapter(List<Store> stores){
            mStores = stores;
        }

        @Override
        public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_main_list, parent, false);

            return new StoreHolder(v);
        }

        @Override
        public void onBindViewHolder(StoreHolder holder, int position){
            Store store = mStores.get(position);
            holder.bindList(store);
        }

        @Override
        public int getItemCount(){
            return mStores.size();
        }

        public void setStores(List<Store> stores){
            mStores = stores;
        }
    }
}

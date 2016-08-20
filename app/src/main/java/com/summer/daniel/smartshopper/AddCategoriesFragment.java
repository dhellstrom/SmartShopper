package com.summer.daniel.smartshopper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-20.
 */
public class AddCategoriesFragment extends Fragment {

    private static final String TAG = "AddCategories";

    private static final String ARG_STORE_ID = "com.summer.daniel.smartshopper.addCategoriesFragment.storeId";
    private static final String DIALOG_NEW_CATEGORY = "NewCategory";

    private static final int REQUEST_CATEGORY = 0;

    private RecyclerView mCategoriesRecyclerView;
    private CategoryAdapter mAdapter;

    private Store mStore;
    private List<String> mAvailableCategories;
    private boolean[] mAddToStoreStatus;

    public static AddCategoriesFragment newInstance(UUID storeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE_ID, storeId);

        AddCategoriesFragment fragment = new AddCategoriesFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID storeId = (UUID) getArguments().getSerializable(ARG_STORE_ID);
        mStore = InformationStorage.get(getActivity()).getStore(storeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        super.onCreateView(inflater, parent, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_add_categories, parent, false);

        mCategoriesRecyclerView = (RecyclerView) v.findViewById(R.id.add_categories_recycler_view);
        mCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_add_categories, menu);

        MenuItem newCategoryItem = menu.findItem(R.id.menu_add_categories_new_category);
        MenuItem confirmItem = menu.findItem(R.id.menu_add_categories_confirm);
        boolean visibleStatus = false;
        for(boolean b : mAddToStoreStatus){
            if(b == true){
                visibleStatus = b;
            }
        }
        newCategoryItem.setVisible(!visibleStatus);
        confirmItem.setVisible(visibleStatus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_add_categories_confirm:
                for(int i = 0; i < mAvailableCategories.size(); i++){
                    if(mAddToStoreStatus[i]) {
                        mStore.addCategory(mAvailableCategories.get(i));
                    }
                }
                InformationStorage.get(getActivity()).updateStore(mStore);
                getActivity().finish();
                return true;
            case R.id.menu_add_categories_new_category:
                FragmentManager manager = getFragmentManager();
                NewCategoryFragment dialog = new NewCategoryFragment();
                dialog.setTargetFragment(AddCategoriesFragment.this, REQUEST_CATEGORY);
                dialog.show(manager, DIALOG_NEW_CATEGORY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CATEGORY){
                String category = data.getStringExtra(NewCategoryFragment.EXTRA_CATEGORY_NAME);
                InformationStorage.get(getActivity()).addCategory(category);
                updateUI();
            }
        }
    }

    private void updateUI(){
        InformationStorage storage = InformationStorage.get(getActivity());
        List<String> savedCategories = storage.getCategories();

        //only display categories not already in the store
        String[] storeCategories = mStore.getCategories();
        mAvailableCategories = new ArrayList<>();
        for(String category : savedCategories){
            if(!arrayContains(storeCategories, category)){
                mAvailableCategories.add(category);
            }
        }

        mAddToStoreStatus = new boolean[mAvailableCategories.size()];

        if(mAdapter == null){
            mAdapter = new CategoryAdapter(mAvailableCategories);
            mCategoriesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setCategories(mAvailableCategories);
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean arrayContains(String[] array, String category){
        for(String s : array){
            if(s.equals(category)){
                return true;
            }
        }
        return false;
    }

    private class CategoryHolder extends RecyclerView.ViewHolder{

        private TextView mCategoryName;
        private CheckBox mAddCheckbox;

        public CategoryHolder(View itemView){
            super(itemView);

            mCategoryName = (TextView) itemView.findViewById(R.id.add_category_item_name);
            mAddCheckbox  = (CheckBox) itemView.findViewById(R.id.add_category_item_check_box);
            mAddCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mAddToStoreStatus[getAdapterPosition()] = b;
                    getActivity().invalidateOptionsMenu();
                }
            });
        }

        public void bindCategory(String category){
            mCategoryName.setText(category);
        }

    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder>{

        private List<String> mCategories;

        public CategoryAdapter(List<String> categories){
            mCategories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_categories,
                    parent, false);
            return new CategoryHolder(v);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            holder.bindCategory(mCategories.get(position));

        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

        public void setCategories(List<String> categories){
            mCategories = categories;
        }
    }


}

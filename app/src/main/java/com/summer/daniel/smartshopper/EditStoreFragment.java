package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Daniel on 2016-08-18.
 */
public class EditStoreFragment extends Fragment {

    private static final String TAG = "EditStore";

    private static final String ARGS_STORE_ID = "com.summer.daniel.smartshopper.editStoreFragment.storeId";
    private static final String KEY_STORE_ID = "id";

    private EditText mNameField;
    private Button mSetLocationButton, mAddCategoryButton;
    private RecyclerView mStoreCategoriesView;

    private Store mStore;
    private CategoryAdapter mAdapter;

    public static EditStoreFragment newInstance(UUID storeId){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_STORE_ID, storeId);
        EditStoreFragment fragment = new EditStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        InformationStorage storage = InformationStorage.get(getActivity());

        UUID storeId;
        if(savedInstanceState != null){
            storeId = (UUID) savedInstanceState.getSerializable(KEY_STORE_ID);
        }else {
          storeId =(UUID) getArguments().getSerializable(ARGS_STORE_ID);
        }
        if(storeId == null){
            mStore = new Store("New Store", null);
            storage.addStore(mStore);
        }else{
            mStore = storage.getStore(storeId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_edit_store, parent, false);

        mNameField = (EditText) v.findViewById(R.id.edit_store_name_field);
        mNameField.setText(mStore.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStore.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
        });

        mSetLocationButton = (Button) v.findViewById(R.id.edit_store_set_location_button);
        mSetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SetLocationActivity.newIntent(getActivity(), mStore.getId());
                startActivity(intent);
            }
        });

        mAddCategoryButton = (Button) v.findViewById(R.id.edit_store_add_category_button);
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddCategoriesActivity.newIntent(getActivity(), mStore.getId());
                startActivity(intent);
            }
        });

        mStoreCategoriesView = (RecyclerView) v.findViewById(R.id.edit_store_category_recycler_view);
        mStoreCategoriesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(R.string.edit_store_title);

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_edit_store, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_delete_store:
                InformationStorage.get(getActivity()).deleteStore(mStore);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        InformationStorage.get(getActivity()).updateStore(mStore);
    }

    @Override
    public void onResume(){
        super.onResume();

        Store temp = InformationStorage.get(getActivity()).getStore(mStore.getId());
        if(temp != null){
            mStore = temp;
        }
        String[] cat = mStore.getCategories();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_STORE_ID, mStore.getId());

    }

    private void updateUI(){
        if(mAdapter == null){
            mAdapter = new CategoryAdapter(mStore.getCategories());
            mStoreCategoriesView.setAdapter(mAdapter);
        }else{
            mAdapter.setCategories(mStore.getCategories());
            mAdapter.notifyDataSetChanged();
        }

    }

    private class CategoryHolder extends RecyclerView.ViewHolder{

        private TextView mCategoryName;
        private ImageButton mIncreasePriorityButton, mDecreasePriorityButton;

        private String mCategory;

        public CategoryHolder(View itemView){
            super(itemView);

            mCategoryName = (TextView) itemView.findViewById(R.id.category_item_name);
            mIncreasePriorityButton = (ImageButton) itemView.findViewById(R.id.category_item_increase_priority);
            mIncreasePriorityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStore.increasePriority(mCategory);
                    updateUI();
                }
            });
            mDecreasePriorityButton = (ImageButton) itemView.findViewById(R.id.category_item_decrease_priority);
            mDecreasePriorityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStore.decreasePriority(mCategory);
                    updateUI();
                }
            });
        }

        public void bindCategory(String category){
            mCategory = category;
            mCategoryName.setText(mCategory);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder>{

        private String[] mCategories;

        public CategoryAdapter(String[] categories){
            mCategories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_category_list, parent, false);

            return new CategoryHolder(v);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            String category = mCategories[position];
            holder.bindCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.length;
        }

        public void setCategories(String[] categories){
            mCategories = categories;
        }
    }
}

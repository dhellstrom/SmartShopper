package com.summer.daniel.smartshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Daniel on 2016-08-18.
 */
public class EditStoreFragment extends Fragment {

    private static final String ARGS_STORE_NAME = "com.summer.daniel.smartshopper.editStoreFragment.storeName";

    private EditText mNameField;
    private Button mSetLocationButton, mAddCategoryButton;

    private Store mStore;
    private CategoryAdapter mAdapter;

    public static Fragment newInstance(String storeName){
        Bundle args = new Bundle();
        args.putString(ARGS_STORE_NAME, storeName);

        EditStoreFragment fragment = new EditStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String storeName = getArguments().getString(ARGS_STORE_NAME);

        if(storeName == null){
            mStore = new Store("New Store", null); //TODO save store to db when going back, and only if location is selected
        }else{
            mStore = InformationStorage.get(getActivity()).getStore(storeName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_edit_store, parent, false);

        mNameField = (EditText) v.findViewById(R.id.edit_store_name_field);
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
                //TODO start LocationActivity
            }
        });

        mAddCategoryButton = (Button) v.findViewById(R.id.edit_store_add_category_button);
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO start addCategoriesActivity
            }
        });

        updateUI();

        return v;
    }

    private void updateUI(){

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

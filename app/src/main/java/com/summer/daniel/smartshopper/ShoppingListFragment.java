package com.summer.daniel.smartshopper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 2016-08-16.
 */
public class ShoppingListFragment extends Fragment {


    private static final int REQUEST_LOCATION_PERMISSION = 0;
    private static final String ARGS_LIST_ID = "com.summer.daniel.smartshopper.shoppingListFragment.list_id";

    private RecyclerView mListContents;
    private ShopItemAdapter mAdapter;

    private GoogleApiClient mClient;

    private ShoppingList mList;

    public static ShoppingListFragment newInstance(UUID listId){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_LIST_ID, listId);

        ShoppingListFragment fragment = new ShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mList = InformationStorage.get(getActivity())
                .getShoppingList((UUID) getArguments().getSerializable(ARGS_LIST_ID));

        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_shopping_list, parent, false);

        mListContents = (RecyclerView) v.findViewById(R.id.shopping_list_recycler_view);
        mListContents.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();

        if(mList != null) {
            InformationStorage.get(getActivity()).updateShoppingList(mList);
        }
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

        MenuItem updateItem = menu.findItem(R.id.menu_update_list);
        updateItem.setEnabled(mClient.isConnected());
    }

    @Override
    public void onStart() {
        super.onStart();

        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_edit_list:
                Intent intent = EditShoppingListActivity.newIntent(getActivity(), mList.getId());
                startActivity(intent);
                return true;
            case R.id.menu_update_list:
                getLocationAndUpdateList();
                return true;
            case R.id.menu_delete_list:
                InformationStorage.get(getActivity()).deleteShoppingList(mList);
                mList = null;
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(mList.getName());

        List<ShopItem> items = mList.getItems();

        if(mAdapter == null){
            mAdapter = new ShopItemAdapter(items);
            mListContents.setAdapter(mAdapter);
        }else{
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocationAndUpdateList();
            }
        }
    }

    /**
     * Checks for permission to use location. Requests permission if it had not been granted earlier.
     *
     */
    private void getLocationAndUpdateList() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            sortListAfterLocation(
                                    new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void sortListAfterLocation(LatLng location){
        Store closestStore = getClosestStore(location);
        if(closestStore != null){
            Toast.makeText(getActivity(), "List sorted for: " + closestStore.getName(),
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), R.string.no_nearby_store_toast, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Returns the store closest to the user's current position.
     * Returns null if there is no store in a 100m radius.
     */
    private Store getClosestStore(LatLng location){
        List<Store> stores = InformationStorage.get(getActivity()).getStores();
        Store closestStore = null;
        float closestDistance = 100;
        float[] distance;
        for(Store store : stores){
            LatLng storeLocation = store.getLocation();
            distance = new float[1];
            Location.distanceBetween(location.latitude, location.longitude,
                    storeLocation.latitude, storeLocation.longitude, distance);
            if(distance[0] < closestDistance){
                closestStore = store;
                closestDistance = distance[0];
            }
        }
        return closestStore;
    }


    private class ShopItemHolder extends RecyclerView.ViewHolder{

        private TextView mNameTextView;
        private TextView mCategoryTextView;
        private CheckBox mPurchasedCheckBox;

        private ShopItem mItem;

        public ShopItemHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.shopping_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.shopping_list_item_category);
            mPurchasedCheckBox = (CheckBox) itemView.findViewById(R.id.shopping_list_item_purchased);
            mPurchasedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mList.toggleItemPurchased(mItem.getName());
                }
            });

        }

        public void bindItem(ShopItem item){
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mCategoryTextView.setText(mItem.getCategory());
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
            View v = inflater.inflate(R.layout.item_shopping_list, parent, false);

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

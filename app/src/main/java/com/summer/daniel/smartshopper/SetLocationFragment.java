package com.summer.daniel.smartshopper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.summer.daniel.smartshopper.model.InformationStorage;
import com.summer.daniel.smartshopper.model.Store;

import java.util.UUID;


/**
 * Created by Daniel on 2016-08-19.
 * Fragment that user uses to select the location of the current store.
 */
public class SetLocationFragment extends SupportMapFragment {


    private static final String ARG_STORE_ID = "com.summer.daniel.smartshopper.setLocationFragment.storeId";
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    private Store mStore;
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private LatLng mDisplayLocation;
    private Marker mMapMarker;

    public static SetLocationFragment newInstance(UUID storeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE_ID, storeId);

        SetLocationFragment fragment = new SetLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID storeId = (UUID) getArguments().getSerializable(ARG_STORE_ID);
        mStore = InformationStorage.get(getActivity()).getStore(storeId);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        placeMarker(latLng);
                        getActivity().invalidateOptionsMenu();
                    }
                });

                if(mStore.hasLocation()){
                    //display store location if available
                    mDisplayLocation = mStore.getLocation();
                    moveToLocation(true);
                }else{
                    //otherwise construct a location client, retrieve the user location and
                    //display it
                    mClient = new GoogleApiClient.Builder(getActivity())
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    //display user location otherwise
                                    getUserLocation();
                                }
                                @Override
                                public void onConnectionSuspended(int i) {
                                    //do nothing
                                }
                            })
                            .build();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mClient != null) {
            mClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mClient != null) {
            mClient.disconnect();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_set_location, menu);

        MenuItem confirmItem = menu.findItem(R.id.menu_confirm_location);
        //confirmItem is only visible when user has placed a marker
        confirmItem.setVisible(mMapMarker != null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_confirm_location:
                mStore.setLocation(mMapMarker.getPosition());
                InformationStorage.get(getActivity()).updateStore(mStore);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                getUserLocation();
            }
        }
    }

    /**
     * Checks for permission to use location. Requests permission if it had not been granted earlier.
     * Gets the user location and moves the map to it.
     */
    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1); //only get one location update and then stop
            request.setInterval(0);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mDisplayLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            moveToLocation(false);
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * Moves the camera to the current displayLocation (mDisplayLocation) and places a marker
     * if placeMarker is true.
     * @param placeMarker whether marker should be placed or not
     */
    private void moveToLocation(boolean placeMarker){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mDisplayLocation, 15);
        mMap.animateCamera(cameraUpdate);
        if(placeMarker){
            placeMarker(mDisplayLocation);
        }
    }

    /**
     * Places a marker at the provided location.
     */
    private void placeMarker(LatLng latLng){
        if(mMapMarker == null){
            mMapMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        }else{
            mMapMarker.setPosition(latLng);
        }
    }
}

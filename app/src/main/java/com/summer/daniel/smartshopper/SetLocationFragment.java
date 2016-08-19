package com.summer.daniel.smartshopper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

import java.util.UUID;


/**
 * Created by Daniel on 2016-08-19.
 */
public class SetLocationFragment extends SupportMapFragment {

    private static final String ARG_STORE_ID = "com.summer.daniel.smartshopper.setLocationFragment.storeId";
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    private Store mStore;
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private LatLng mDisplayLocation;

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

        UUID storeId = (UUID) getArguments().getSerializable(ARG_STORE_ID);
        mStore = InformationStorage.get(getActivity()).getStore(storeId);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (mStore.hasLocation()) {
                            //display store location if available
                            mDisplayLocation = mStore.getLocation();
                        } else {
                            //display user location otherwise
                            getUserLocation();
                        }
                        if (mDisplayLocation != null) {
                            moveToLocation();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        //do nothing
                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getUserLocation();
            }
        }
    }

    private void getUserLocation() {
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
                            mDisplayLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void moveToLocation(){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mDisplayLocation, 15);
        mMap.animateCamera(cameraUpdate);
    }




}

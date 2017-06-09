package bluenergyfuel.bluenergy.drawer.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationLocator extends BaseFragment implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private GoogleMap mMap;
    private DatabaseReference mRef;
    private ArrayList<String> mapsLocations = new ArrayList<>();
    private final int PERMISSION_LOCATION = 111;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mapFragment;
    private static final String TAG = "StationLocator";

    public StationLocator() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_station_locator, container, false);
        mRef = MyDatabaseUtil.getDatabase().getReference().child("station");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                }else {
                    //show a dialog ... if permission is denied
                    Log.d(TAG, "I forgot a dialog");
                }
            }
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                makeToast("We Have "+String.valueOf(dataSnapshot.getChildrenCount())+" Stations");
                stationLocations((Map<String, Object>) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void startLocationServices() {
        Log.d(TAG, "Location Services called");

        try {
            LocationRequest locationRequest = LocationRequest
                    .create()
                    .setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        } catch (SecurityException e) {
            Log.d(TAG, e.toString());
            Toast.makeText(getActivity(), "We Need Permission For This Activity", Toast.LENGTH_SHORT).show();
        }
    }

    private void stationLocations(Map<String, Object> locationsLocations) {
        for (Map.Entry<String, Object> entry : locationsLocations.entrySet()) {
            Map singleLocation = (Map) entry.getValue();
            mapsLocations.add((String) singleLocation.get("location"));
        }
    }

    private void checkingLocation() {
        for (String temp : mapsLocations) {
            StringTokenizer stringTokenizer = new StringTokenizer(temp, "_");
            String latitude = stringTokenizer.nextToken();
            String longitude = stringTokenizer.nextToken();
            String titleSet = stringTokenizer.nextToken();
            LatLng tempLatLon = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            mMap.addMarker(new MarkerOptions().position(tempLatLon).title(titleSet).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)));
        }

    }
    public void setUserMarker(LatLng latLng) {
        mMap.clear();
        checkingLocation();
        MarkerOptions userMarker = new MarkerOptions().position(latLng).title("You Are Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_current_location)).zIndex(1.0f);
        mMap.addMarker(userMarker);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.d(TAG, "Persmission Request");
        }else {
            Log.d(TAG, "Nothing my man");
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        setUserMarker(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }
    //This is a simple Fix for switching fragments
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        if (this.mapFragment != null
                && getFragmentManager().findFragmentById(
                this.mapFragment.getId()) != null) {
            getFragmentManager().beginTransaction().remove(this.mapFragment)
                    .commit();
            this.mapFragment = null;
        }
    }
}

package com.example.trackensuretest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RefuelEditorActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final double TEST_LATITUDE = 50.44;
    public static final double TEST_LONGITUDE = 30.51;
    public static final int MAP_ZOOM = 17;
    private MapView mapView;
    private GoogleMap googleMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_editor);
        mapView = findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        context = this;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private String getAddressFromCoordinates (Context context, double lat, double lng){
        String address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList.size() > 0)
                address = addressList.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMapLongClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TEST_LATITUDE, TEST_LONGITUDE), MAP_ZOOM));
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the gas station");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String gasStationAddress = getAddressFromCoordinates(context, latLng.latitude, latLng.longitude);
                if (gasStationAddress != null) {
                    /*GasStation gasStation = new GasStation(String.valueOf(input.getText()), gasStationAddress);
                    AppDatabase appDatabase = App.getInstance().getAppDatabase();
                    GasStationDao gasStationDao = appDatabase.gasStationDao();
                    gasStationDao.insert(gasStation);*/

                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(String.valueOf(input.getText())));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

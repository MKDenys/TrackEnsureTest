package com.example.trackensuretest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RefuelEditorActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final double TEST_LATITUDE = 50.44;
    public static final double TEST_LONGITUDE = 30.51;
    public static final int MAP_ZOOM = 17;
    private MapView mapView;
    private GoogleMap googleMap;
    private Context context;
    private EditText fuelSupplierEditText;
    private Spinner fuelTypeSpinner;
    private EditText amountEditText;
    private EditText priceEditText;
    private Button saveButton;
    private Button closeButton;
    private GasStation gasStationToSave;
    private Refuel oldRefuel;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_editor);

        mapView = findViewById(R.id.mapView);
        fuelSupplierEditText = findViewById(R.id.fuel_supplier_name_editText);
        fuelTypeSpinner = findViewById(R.id.fuel_type_spinner);
        fuelTypeSpinner.setAdapter(new ArrayAdapter<FuelType>(this, android.R.layout.simple_spinner_item, FuelType.values()));
        amountEditText = findViewById(R.id.fuel_amount_editText);
        priceEditText = findViewById(R.id.price_editText);
        saveButton = findViewById(R.id.save_button);
        closeButton = findViewById(R.id.close_button);

        saveButton.setOnClickListener(saveButtonClick);
        closeButton.setOnClickListener(closeButtonClick);

        gasStationToSave = null;
        isEdit = false;
        oldRefuel = null;
        try {
            oldRefuel = (Refuel) getIntent().getExtras().get("refuel");
            isEdit = true;
        } catch (Exception ex) {}

        if (oldRefuel != null){
            fuelSupplierEditText.setText(oldRefuel.getFuelSupplierName());
            fuelTypeSpinner.setSelection(FuelType.valueOf(oldRefuel.getFuelType()).ordinal());
            amountEditText.setText(String.valueOf(oldRefuel.getAmount()));
            priceEditText.setText(String.valueOf(oldRefuel.getPrice()));

            AppDatabase appDatabase = App.getInstance().getAppDatabase();
            gasStationToSave = appDatabase.gasStationDao().getById(oldRefuel.getGasStationId());
        }

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
        googleMap.setOnMarkerClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TEST_LATITUDE, TEST_LONGITUDE), MAP_ZOOM));
        AppDatabase appDatabase = App.getInstance().getAppDatabase();
        GasStationDao gasStationDao = appDatabase.gasStationDao();
        gasStationDao.getAll().observe(this, new Observer<List<GasStation>>() {
            @Override
            public void onChanged(List<GasStation> gasStationList) {
                if (gasStationList.size() > 0){
                    for (int i = 0; i < gasStationList.size(); i++) {
                        GasStation gasStation = gasStationList.get(i);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(gasStation.getLatitude(), gasStation.getLongitude()))
                                .title(gasStation.getName()));
                    }
                }
            }
        });
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
                    GasStation gasStation = new GasStation(String.valueOf(input.getText()), gasStationAddress,
                            latLng.latitude, latLng.longitude);
                    AppDatabase appDatabase = App.getInstance().getAppDatabase();
                    GasStationDao gasStationDao = appDatabase.gasStationDao();
                    gasStationDao.insert(gasStation);
                    gasStationToSave = gasStation;

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

    private final View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (gasStationToSave != null) {
                if (!fuelSupplierEditText.getText().toString().isEmpty() &&
                        !amountEditText.getText().toString().isEmpty() &&
                        !priceEditText.getText().toString().isEmpty()) {
                    Refuel refuel = new Refuel(fuelSupplierEditText.getText().toString(),
                            fuelTypeSpinner.getSelectedItem().toString(),
                            Double.parseDouble(amountEditText.getText().toString()),
                            Double.parseDouble(priceEditText.getText().toString()),
                            gasStationToSave.getId());
                    AppDatabase appDatabase = App.getInstance().getAppDatabase();
                    RefuelDao refuelDao = appDatabase.refuelDao();
                    if (isEdit){
                        refuel.setId(oldRefuel.getId());
                        refuelDao.update(refuel);
                    } else {
                        refuelDao.insert(refuel);
                    }
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Select gas station on the map", Toast.LENGTH_LONG).show();
            }
        }
    };

    private final View.OnClickListener closeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        AppDatabase appDatabase = App.getInstance().getAppDatabase();
        GasStationDao gasStationDao = appDatabase.gasStationDao();
        gasStationToSave = gasStationDao.getByName(marker.getTitle());
        return false;
    }
}

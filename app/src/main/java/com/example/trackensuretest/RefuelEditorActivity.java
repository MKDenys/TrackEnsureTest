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

import com.example.trackensuretest.database.GasStationRepository;
import com.example.trackensuretest.database.RefuelRepository;
import com.example.trackensuretest.models.FuelType;
import com.example.trackensuretest.models.GasStation;
import com.example.trackensuretest.models.Refuel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RefuelEditorActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final double TEST_LATITUDE = 50.44;
    private static final double TEST_LONGITUDE = 30.51;
    private static final int MAP_ZOOM = 17;
    private static final String AUTH_DIALOG_MESSAGE ="Sing in, please, to sync data with remote database";
    public static final String AUTH_DIALOG_TITLE = "Sing In";
    private MapView mapView;
    private GoogleMap googleMap;
    private Context context;
    private EditText fuelSupplierEditText;
    private Spinner fuelTypeSpinner;
    private EditText amountEditText;
    private EditText priceEditText;
    private GasStation gasStationToSave;
    private Refuel oldRefuel;
    private boolean isEdit;
    private GasStationRepository gasStationRepository;
    private RefuelRepository refuelRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_editor);

        mapView = findViewById(R.id.mapView);
        fuelSupplierEditText = findViewById(R.id.fuel_supplier_name_editText);
        fuelTypeSpinner = findViewById(R.id.fuel_type_spinner);
        fuelTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FuelType.values()));
        amountEditText = findViewById(R.id.fuel_amount_editText);
        priceEditText = findViewById(R.id.price_editText);
        Button saveButton = findViewById(R.id.save_button);
        Button closeButton = findViewById(R.id.close_button);
        saveButton.setOnClickListener(saveButtonClick);
        closeButton.setOnClickListener(closeButtonClick);
        gasStationRepository = new GasStationRepository();
        refuelRepository = new RefuelRepository();
        if (getUserId().isEmpty()){
            showAlertDialog();
        }
        gasStationToSave = null;
        isEdit = false;
        oldRefuel = null;
        getDataFromIntent();
        initGoogleMap(savedInstanceState);
        context = this;
    }

    private void getDataFromIntent(){
        try {
            oldRefuel = getIntent().getExtras().getParcelable(Constants.IS_EDIT_KEY);
            isEdit = true;
            fuelSupplierEditText.setText(oldRefuel.getFuelSupplierName());
            fuelTypeSpinner.setSelection(FuelType.valueOf(oldRefuel.getFuelType()).ordinal());
            amountEditText.setText(String.valueOf(oldRefuel.getAmount()));
            priceEditText.setText(String.valueOf(oldRefuel.getPrice()));
            gasStationRepository.getByIdTask((int) oldRefuel.getGasStationId()).observe(this,
                    new Observer<GasStation>() {
                        @Override
                        public void onChanged(GasStation gasStation) {
                            gasStationToSave = gasStation;
                        }
                    });
        } catch (Exception ignored) {}
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(AUTH_DIALOG_MESSAGE).setTitle(AUTH_DIALOG_TITLE);
        builder.setPositiveButton(Constants.POSITIVE_BUTTON_TITLE, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(RefuelEditorActivity.this, MainActivity.class);
                intent.putExtra(Constants.OPEN_SIGN_IN_DIALOG_KEY, true);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(Constants.NEGATIVE_BUTTON_TITLE, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
        gasStationRepository.getAllTask().observe(this, new Observer<List<GasStation>>() {
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

        builder.setPositiveButton(Constants.POSITIVE_BUTTON_TITLE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gasStationAddress = getAddressFromCoordinates(context, latLng.latitude, latLng.longitude);
                if (gasStationAddress != null) {
                    gasStationToSave = new GasStation(
                            String.valueOf(input.getText()),
                            gasStationAddress,
                            latLng.latitude,
                            latLng.longitude);
                    insertGasStationToDB(gasStationToSave);

                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(String.valueOf(input.getText())));
                }
            }
        });

        builder.setNegativeButton(Constants.NEGATIVE_BUTTON_TITLE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void insertGasStationToDB(GasStation gasStation){
        gasStationRepository.insertTask(gasStation);
        if (getUserId().isEmpty()) {
            Toast.makeText(this, "GasStation added to local database only. Sign in, please",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void insertRefuelToDB(Refuel refuel){
        if (isEdit){
            refuel.setId(oldRefuel.getId());
            refuel.setSynced(false);
            refuelRepository.updateTask(refuel);
            if (getUserId().isEmpty()) {
                Toast.makeText(this, "Refuel update in local database only. Sign in, please",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            refuelRepository.insertTask(refuel);
            if (getUserId().isEmpty()) {
                Toast.makeText(this, "Refuel added to local database only. Sign in, please",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private final View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (gasStationToSave != null) {
                if (!fuelSupplierEditText.getText().toString().isEmpty() &&
                        !amountEditText.getText().toString().isEmpty() &&
                        !priceEditText.getText().toString().isEmpty()) {
                    Refuel refuel = new Refuel(
                            fuelSupplierEditText.getText().toString(),
                            fuelTypeSpinner.getSelectedItem().toString(),
                            Double.parseDouble(amountEditText.getText().toString()),
                            Double.parseDouble(priceEditText.getText().toString()),
                            gasStationToSave.getId());
                    insertRefuelToDB(refuel);
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
        gasStationRepository.getByNameTask(marker.getTitle()).observe(this,
                new Observer<GasStation>() {
                    @Override
                    public void onChanged(GasStation gasStation) {
                        gasStationToSave = gasStation;
                    }
                });
        return false;
    }

    private String getUserId() {
        String userId = "";
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception ignored){ }
        return userId;
    }
}

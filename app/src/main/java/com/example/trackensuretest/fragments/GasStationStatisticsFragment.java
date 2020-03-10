package com.example.trackensuretest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.trackensuretest.R;
import com.example.trackensuretest.database.GasStationRepository;
import com.example.trackensuretest.database.RefuelRepository;
import com.example.trackensuretest.models.GasStation;
import com.example.trackensuretest.models.Refuel;

import java.util.ArrayList;
import java.util.List;

public class GasStationStatisticsFragment extends PlaceholderFragment{
    private static final String COLUMN_NAME_TITLE = "Name";
    private static final String COLUMN_ADDRESS_TITLE = "Address";
    protected static final String COLUMN_REFUELS_TITLE = "Refuels";
    private TableLayout tableLayout;
    private RefuelRepository refuelRepository;
    private GasStationRepository gasStationRepository;
    private List<Refuel> refuels = new ArrayList<>();
    private List<GasStation> gasStations = new ArrayList<>();

    public GasStationStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gas_station_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tableLayout = view.findViewById(R.id.statisticsTable);
        refuelRepository = new RefuelRepository();
        gasStationRepository = new GasStationRepository();
        observeToGasStations();
        observeToRefuels();
    }

    private void observeToGasStations(){
        gasStationRepository.getAllTask().observe(getActivity(), new Observer<List<GasStation>>() {
            @Override
            public void onChanged(List<GasStation> gasStationList) {
                if(gasStations.size() > 0){
                    gasStations.clear();
                }
                if(gasStationList != null){
                    gasStations.addAll(gasStationList);
                    fillTable();
                }
            }
        });
    }

    private void observeToRefuels(){
        refuelRepository.getAllTask().observe(getActivity(), new Observer<List<Refuel>>() {
            @Override
            public void onChanged(List<Refuel> refuelList) {
                if(refuels.size() > 0){
                    refuels.clear();
                }
                if(refuelList != null){
                    refuels.addAll(refuelList);
                    fillTable();
                }
            }
        });
    }

    private int countRefuelsInGasStation(GasStation gasStation){
        int counter = 0;
        for (int i = 0; i < refuels.size(); i++){
            if(refuels.get(i).getGasStationId() == gasStation.getId())
                counter++;
        }
        return counter;
    }

    private void fillTable(){
        tableLayout.removeAllViews();
        String[] elementText = new String[] {COLUMN_NAME_TITLE, COLUMN_ADDRESS_TITLE, COLUMN_REFUELS_TITLE};
        addRow(tableLayout, elementText);
        for (int i = 0; i < gasStations.size(); i++){
            String gasStationAddress = gasStations.get(i).getAddress();
            String gasStationName = gasStations.get(i).getName();
            int refuelsCounter = countRefuelsInGasStation(gasStations.get(i));
            elementText = new String[] {gasStationName, gasStationAddress, String.valueOf(refuelsCounter)};
            addRow(tableLayout, elementText);
        }
    }

    private void addRow(TableLayout target, String[] elementText){
        TableRow tableRow = new TableRow(getActivity());
        for (int j = 0; j < elementText.length; j++) {
            TextView textView = new TextView(getActivity());
            textView.setTextSize(15);
            textView.setText(elementText[j]);
            textView.setPadding(15, 10, 15, 10);
            tableRow.addView(textView);
        }
        target.addView(tableRow);
    }
}

package com.example.trackensuretest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.trackensuretest.ui.main.PlaceholderFragment;

import java.util.List;

public class GasStationStatisticsFragment extends PlaceholderFragment{
    private TableLayout tableLayout;

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
        View root = inflater.inflate(R.layout.fragment_gas_station_statistics, container, false);
        tableLayout = root.findViewById(R.id.statisticsTable);

        AppDatabase appDatabase = App.getInstance().getAppDatabase();
        GasStationDao gasStationDao = appDatabase.gasStationDao();
        gasStationDao.getAll().observe(getActivity(), new Observer<List<GasStation>>() {
            @Override
            public void onChanged(List<GasStation> gasStationList) {
                fillTable();
            }
        });
        RefuelDao refuelDao = appDatabase.refuelDao();
        refuelDao.getAll().observe(getActivity(), new Observer<List<Refuel>>() {
            @Override
            public void onChanged(List<Refuel> refuelList) {
                fillTable();
            }
        });
        return root;
    }

    private void fillTable(){
        GasStationDao gasStationDao = App.getInstance().getAppDatabase().gasStationDao();
        gasStationDao.getAll().observe(getActivity(), new Observer<List<GasStation>>() {
            @Override
            public void onChanged(List<GasStation> gasStationList) {
                tableLayout.removeAllViews();
                RefuelDao refuelDao = App.getInstance().getAppDatabase().refuelDao();
                for (int i = 0; i < gasStationList.size(); i++){
                    String gasStationAddress = gasStationList.get(i).getAddress();
                    String refuelCount = String.valueOf(refuelDao.getCountRefuelsInGasStation(gasStationList.get(i).getId()));
                    String[] elementText = new String[] {gasStationAddress, refuelCount};
                    TableRow tableRow = new TableRow(getActivity());
                    for (int j = 0; j < elementText.length; j++) {
                        TextView textView = new TextView(getActivity());
                        textView.setTextSize(15);
                        textView.setText(elementText[j]);
                        textView.setPadding(30, 10, 30, 10);
                        tableRow.addView(textView);
                    }
                    tableLayout.addView(tableRow);
                }
            }
        });
    }
}

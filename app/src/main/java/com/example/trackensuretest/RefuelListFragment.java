package com.example.trackensuretest;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.trackensuretest.ui.main.PlaceholderFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class RefuelListFragment extends PlaceholderFragment {

    public RefuelListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView;
        RefuelListAdapter refuelListAdapter;
        View root = inflater.inflate(R.layout.fragment_refuel_list, container, false);
        FloatingActionButton addRefuelButton = root.findViewById(R.id.addRefuelButton);
        addRefuelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RefuelEditorActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = root.findViewById(R.id.recyclerviev);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Refuel> refuelList = new ArrayList<>();
        GasStation gasStation = new GasStation("station ONE", "1111");
        for (int i = 0; i < 10; i++){
            Refuel refuel = new Refuel("DDD", FuelType.DIESEL, 3.9 + i, 1.04, gasStation);
            refuelList.add(refuel);
        }

        refuelListAdapter = new RefuelListAdapter(refuelList);
        recyclerView.setAdapter(refuelListAdapter);

        return root;
    }
}

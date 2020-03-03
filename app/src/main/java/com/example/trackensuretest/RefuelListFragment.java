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

        AppDatabase appDatabase = App.getInstance().getAppDatabase();
        RefuelDao refuelDao = appDatabase.refuelDao();
        List<Refuel> refuelList = refuelDao.getAll();


        refuelListAdapter = new RefuelListAdapter(getActivity(), refuelList);
        recyclerView.setAdapter(refuelListAdapter);

        return root;
    }
}

package com.example.trackensuretest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackensuretest.Constants;
import com.example.trackensuretest.R;
import com.example.trackensuretest.RefuelEditorActivity;
import com.example.trackensuretest.adapters.RefuelListAdapter;
import com.example.trackensuretest.database.RefuelRepository;
import com.example.trackensuretest.models.Refuel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class RefuelListFragment extends PlaceholderFragment implements RefuelListAdapter.OnEditButtonClickListener,
        RefuelListAdapter.OnDelButtonClickListener{
    private RefuelListAdapter refuelListAdapter;
    private RefuelRepository refuelRepository;
    private List<Refuel> refuels = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_refuel_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton addRefuelButton = view.findViewById(R.id.addRefuelButton);
        addRefuelButton.setOnClickListener(addRefuelButtonClick);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviev);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refuelListAdapter = new RefuelListAdapter(refuels,
                RefuelListFragment.this,
                RefuelListFragment.this);
        recyclerView.setAdapter(refuelListAdapter);
        refuelRepository = new RefuelRepository();
        observeToRefuels();
    }

    private void observeToRefuels(){
        refuelRepository.getAllTask().observe(getViewLifecycleOwner(), new Observer<List<Refuel>>() {
            @Override
            public void onChanged(List<Refuel> refuelList) {
                if(refuels.size() > 0){
                    refuels.clear();
                }
                if(refuelList != null){
                    refuels.addAll(refuelList);
                    refuelListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private View.OnClickListener addRefuelButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), RefuelEditorActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void OnEditButtonClick(int position) {
        Intent intent = new Intent(getActivity(), RefuelEditorActivity.class);
        intent.putExtra(Constants.IS_EDIT_KEY, refuels.get(position));
        startActivity(intent);
    }

    @Override
    public void OnDelButtonClick(int position) {
        if (!refuels.get(position).isSynced()){
            refuelRepository.deleteTask(refuels.get(position));
        } else {
            refuels.get(position).setDeleted(true);
            refuelRepository.updateTask(refuels.get(position));
        }
    }
}

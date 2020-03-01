package com.example.trackensuretest.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.trackensuretest.GasStationStatisticsFragment;
import com.example.trackensuretest.R;
import com.example.trackensuretest.RefuelListFragment;

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlaceholderFragment newInstance(int index) {
        switch (index){
            case 1:
                return new RefuelListFragment();
            case 2:
                return new GasStationStatisticsFragment();
            default:
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_SECTION_NUMBER, index);
                fragment.setArguments(bundle);
                return fragment;
        }
    }
}
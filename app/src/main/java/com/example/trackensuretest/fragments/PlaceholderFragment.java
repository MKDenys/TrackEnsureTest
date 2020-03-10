package com.example.trackensuretest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
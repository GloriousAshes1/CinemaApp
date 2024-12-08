package com.example.finalprojectmobileapplication.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.finalprojectmobileapplication.databinding.FragmentAdminBookingBinding;

public class AdminBookingFragment extends Fragment {

    private FragmentAdminBookingBinding fragmentAdminBookingBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAdminBookingBinding = FragmentAdminBookingBinding.inflate(inflater, container, false);

        return fragmentAdminBookingBinding.getRoot();
    }
}
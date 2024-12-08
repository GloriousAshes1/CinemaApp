package com.example.finalprojectmobileapplication.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.finalprojectmobileapplication.databinding.FragmentAdminManageBinding;

public class AdminManageFragment extends Fragment {

    private FragmentAdminManageBinding fragmentAdminManageBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAdminManageBinding = FragmentAdminManageBinding.inflate(inflater, container, false);

        return fragmentAdminManageBinding.getRoot();
    }
}
package com.moutamid.homecarepro.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.homecarepro.R;
import com.moutamid.homecarepro.adapter.TaskAdapter;
import com.moutamid.homecarepro.databinding.FragmentAllTaskBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import java.util.ArrayList;

public class AllTaskFragment extends Fragment {
    FragmentAllTaskBinding binding;

    public AllTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllTaskBinding.inflate(getLayoutInflater(), container, false);

        ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        TaskAdapter adapter = new TaskAdapter(requireContext(), list, list);
        binding.recycler.setAdapter(adapter);

        return binding.getRoot();
    }
}
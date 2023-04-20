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
import com.moutamid.homecarepro.databinding.FragmentCompletedBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import java.util.ArrayList;

public class CompletedFragment extends Fragment {

    FragmentCompletedBinding binding;

    public CompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompletedBinding.inflate(getLayoutInflater(), container, false);
        ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);
        ArrayList<TaskModel> completed = new ArrayList<>();

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        for (int i=0; i<list.size(); i++){
            if (list.get(i).isStatus()){
                completed.add(list.get(i));
            }
        }

        TaskAdapter adapter = new TaskAdapter(requireContext(), completed, list);
        binding.recycler.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
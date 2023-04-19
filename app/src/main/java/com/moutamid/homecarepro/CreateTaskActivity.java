package com.moutamid.homecarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.chip.Chip;
import com.moutamid.homecarepro.databinding.ActivityCreateTaskBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CreateTaskActivity extends AppCompatActivity {
    ActivityCreateTaskBinding binding;
    TaskModel taskModel;
    ArrayList<TaskModel> list;
    String frequency = "", priority = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.createTask.setOnClickListener(v -> {
            if (valid()){
                taskModel = new TaskModel(
                        UUID.randomUUID().toString(),
                        binding.taskName.getEditText().getText().toString(),
                        binding.taskDesc.getEditText().getText().toString(),
                        new Date().getTime(),
                        priority, frequency, false
                );
                list.add(taskModel);
                Stash.put(Constants.SAVE_LIST, list);
                Toast.makeText(this, "Task added Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });



    }

    private boolean valid() {
        if (binding.taskName.getEditText().getText().toString().isEmpty()){
            binding.taskName.getEditText().requestFocus();
            Toast.makeText(this, "Please add a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.taskDesc.getEditText().getText().toString().isEmpty()){
            binding.taskDesc.getEditText().requestFocus();
            Toast.makeText(this, "Please add a description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (frequency.isEmpty()) {
            for (int i = 0; i < binding.frequencyChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.frequencyChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    frequency = chip.getText().toString();
                }
            }
            if (frequency.isEmpty()){
                Toast.makeText(this, "Please add a Frequency", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (priority.isEmpty()){
            for (int i = 0; i < binding.priorityChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.priorityChipGroup.getChildAt(i);
                if (chip.isChecked()){
                    priority = chip.getText().toString();
                }
            }
            if (priority.isEmpty()){
                Toast.makeText(this, "Please add a Priority", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
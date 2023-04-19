package com.moutamid.homecarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.homecarepro.databinding.ActivityDateTaskBinding;

public class DateTaskActivity extends AppCompatActivity {
    ActivityDateTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
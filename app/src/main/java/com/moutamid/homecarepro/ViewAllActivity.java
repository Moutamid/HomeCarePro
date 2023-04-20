package com.moutamid.homecarepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moutamid.homecarepro.databinding.ActivityViewAllBinding;
import com.moutamid.homecarepro.fragments.AllTaskFragment;
import com.moutamid.homecarepro.fragments.CompletedFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {
    ActivityViewAllBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new AllTaskFragment()).commit();

        binding.allTask.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new AllTaskFragment()).commit();
            binding.allView.setVisibility(View.VISIBLE);
            binding.complView.setVisibility(View.GONE);
        });

        binding.completedTask.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new CompletedFragment()).commit();
            binding.allView.setVisibility(View.GONE);
            binding.complView.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
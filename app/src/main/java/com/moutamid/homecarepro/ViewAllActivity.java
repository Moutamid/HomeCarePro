package com.moutamid.homecarepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

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

        binding.back.setOnClickListener(v -> onBackPressed());

        setupViewPager(binding.viewPager);
        binding.tabs.setupWithViewPager(binding.viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new AllTaskFragment(), "All Task");
        adapter.addFragment(new CompletedFragment(), "Completed Task");

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }

}
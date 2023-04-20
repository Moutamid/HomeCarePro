package com.moutamid.homecarepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fxn.stash.Stash;
import com.moutamid.homecarepro.adapter.TaskAdapter;
import com.moutamid.homecarepro.databinding.ActivityDateTaskBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateTaskActivity extends AppCompatActivity {
    ActivityDateTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);
        ArrayList<TaskModel> newList = new ArrayList<>();
        String date = Stash.getString(Constants.DATE_CLICK, "");

        SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat);
        SimpleDateFormat calformat = new SimpleDateFormat(Constants.calFormat);
        Date date1, date2;
        String cur = "";
        try {
            date1 = calformat.parse(date);
            String dd = format.format(date1);
            String ff = format.format(new Date().getTime());
            date2 = format.parse(ff);
            cur = format.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        binding.date.setText(cur);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        for (int i = 0; i <list.size(); i++) {
            String s = list.get(i).getStartingDate();
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(list.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
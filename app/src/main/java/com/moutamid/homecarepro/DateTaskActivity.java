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
    ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);
    ArrayList<TaskModel> week = Stash.getArrayList(Constants.WEEKLY_LIST, TaskModel.class);
    ArrayList<TaskModel> month = Stash.getArrayList(Constants.MONTHLY_LIST, TaskModel.class);
    ArrayList<TaskModel> month3 = Stash.getArrayList(Constants.MONTH3_LIST, TaskModel.class);
    ArrayList<TaskModel> newList = new ArrayList<>();
    Date date1;
    String date = Stash.getString(Constants.DATE_CLICK, "");
    SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat);
    SimpleDateFormat calformat = new SimpleDateFormat(Constants.calFormat);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String cur = "";
        try {
            date1 = calformat.parse(date);
            cur = format.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        binding.date.setText(cur);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        for (int i = 0; i <list.size(); i++) {
            String s = format.format(list.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(list.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        checkFromWeek();

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private void checkFromWeek() {
        for (int i = 0; i <week.size(); i++) {
            String s = format.format(week.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(week.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        checkFromMonth();
    }

    private void checkFromMonth() {
        for (int i = 0; i <month.size(); i++) {
            String s = format.format(month.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(month.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        checkFrom3Month();
    }

    private void checkFrom3Month() {
        for (int i = 0; i <month3.size(); i++) {
            String s = format.format(month3.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(month3.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
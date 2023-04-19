package com.moutamid.homecarepro;

import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.events.calendar.views.EventsCalendar;
import com.fxn.stash.Stash;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.OutDateStyle;
import com.kizitonwose.calendar.view.DaySize;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.moutamid.homecarepro.adapter.TaskAdapter;
import com.moutamid.homecarepro.caleder.DayViewContainer;
import com.moutamid.homecarepro.databinding.ActivityMainBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import org.threeten.bp.YearMonth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements EventsCalendar.Callback {
    ActivityMainBinding binding;
    ArrayList<TaskModel> list, todayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        todayList = new ArrayList<>();
        list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        binding.calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay calendarDay) {
                SimpleDateFormat format = new SimpleDateFormat(Constants.calFormat);
                String d = format.format(new Date().getTime());
                String dd = calendarDay.getDate().toString();
                Date date, calDate;
                try {
                    date = format.parse(d);
                    calDate = format.parse(dd);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (date.compareTo(calDate) == 0) {
                    ((TextView) container.getView()).setBackgroundResource(R.drawable.daybg_cur);
                }
                ((TextView) container.getView()).setText(calendarDay.getDate().getDayOfMonth() + "");

                ((TextView) container.getView()).setOnClickListener(v -> {
                    Stash.put(Constants.DATE_CLICK, calendarDay.getDate().toString());
                    startActivity(new Intent(MainActivity.this, DateTaskActivity.class));
                });

            }
        });
        //binding.calendarView.setDaySize(DaySize.Rectangle);
        binding.calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<DayViewContainer>() {

            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarMonth calendarMonth) {
                ((TextView) container.getView()).setText(calendarMonth.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + "");
            }
        });

        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        java.time.YearMonth startMonth = currentMonth.minusMonths(100);
        java.time.YearMonth endMonth = currentMonth.plusMonths(100);
        DayOfWeek firstDayOfWeek = firstDayOfWeekFromLocale();

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        binding.calendarView.scrollToMonth(currentMonth);


        binding.createTask.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateTaskActivity.class));
        });
        binding.viewAll.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewAllActivity.class));
        });

    }

    private void showList() {
//        SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat);
//        String cur = format.format(new Date().getTime());
//        todayList.clear();
//        for (int i=0; i<list.size(); i++) {
//            String date = format.format(list.get(i).getDate());
//            if (date.equals(cur)) {
//                todayList.add(list.get(i));
//                TaskAdapter adapter = new TaskAdapter(this, todayList);
//                binding.recycler.setAdapter(adapter);
//            }
//        }

        TaskAdapter adapter = new TaskAdapter(this, list);
        binding.recycler.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    @Override
    public void onDayLongPressed(@Nullable Calendar calendar) {

    }

    @Override
    public void onDaySelected(@Nullable Calendar calendar) {

    }

    @Override
    public void onMonthChanged(@Nullable Calendar calendar) {

    }


}
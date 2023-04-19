package com.moutamid.homecarepro;

import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.OutDateStyle;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.moutamid.homecarepro.adapter.TaskAdapter;
import com.moutamid.homecarepro.caleder.DayViewContainer;
import com.moutamid.homecarepro.databinding.ActivityMainBinding;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import org.threeten.bp.YearMonth;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<TaskModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        TaskAdapter adapter = new TaskAdapter(this, list);
        binding.recycler.setAdapter(adapter);

        binding.calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
               return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay calendarDay) {
                Calendar cal = Calendar.getInstance();
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                if (calendarDay.getDate().getDayOfMonth() == dayOfMonth){
                    ((TextView) container.getView()).setBackgroundResource(R.drawable.daybg_cur);
                }
                ((TextView) container.getView()).setText(calendarDay.getDate().getDayOfMonth()+"");
            }
        });

        binding.calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<DayViewContainer>() {

            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarMonth calendarMonth) {
                ((TextView) container.getView()).setText(calendarMonth.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())+"");
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

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
package com.moutamid.homecarepro;

import static android.content.ContentValues.TAG;
import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.events.calendar.views.EventsCalendar;
import com.fxn.stash.Stash;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    ArrayList<TaskModel> another;
    SimpleDateFormat format = new SimpleDateFormat(Constants.monthFORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        Date date = new Date();
        String d = format.format(date);
        binding.calendarDayText.setText(d);

        another = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        showList();

        final CompactCalendarView compactCalendarView = (CompactCalendarView) binding.compactcalendarView;
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        for (int i=0; i < another.size(); i++) {
            if (another.get(i).getPriority().equals(Constants.HIGH)) {
                Event ev1 = new Event(getResources().getColor(R.color.high_prio), another.get(i).getStartingDateTimeStamp(), "High Priority Task.");
                compactCalendarView.addEvent(ev1);
            }
            if (another.get(i).getPriority().equals(Constants.MEDIUM)) {
                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), another.get(i).getStartingDateTimeStamp(), "Medium Priority Task.");
                compactCalendarView.addEvent(ev1);
            }
            if (another.get(i).getPriority().equals(Constants.LOW)) {
                Event ev1 = new Event(getResources().getColor(R.color.low_prio), another.get(i).getStartingDateTimeStamp(), "Low Priority Task.");
                compactCalendarView.addEvent(ev1);
            }
        }

       // List<Event> events = compactCalendarView.getEvents(list.get(3).getStartingDateTimeStamp()); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
       // Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                String d = new SimpleDateFormat(Constants.calFormat).format(dateClicked);
                Stash.put(Constants.DATE_CLICK, d);
                startActivity(new Intent(MainActivity.this, DateTaskActivity.class));
                finish();
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date date) {
                String d = format.format(date);
                binding.calendarDayText.setText(d);
                Log.d("DateCh1", "Month was scrolled to: " + date);
            }
        });

        /*

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
                    Log.d("DateCh1", date.toString()+"  " + calDate.toString());
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
*/

        binding.createTask.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateTaskActivity.class));
            finish();
        });

        binding.viewAll.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewAllActivity.class));
            finish();
        });

    }

    private void showList() {
        ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);
        ArrayList<TaskModel> newList = new ArrayList<>();
        String date = new SimpleDateFormat(Constants.calFormat).format(new Date().getTime());

        SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat);
        SimpleDateFormat calformat = new SimpleDateFormat(Constants.calFormat);
        Date date1;
        try {
            date1 = calformat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

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

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
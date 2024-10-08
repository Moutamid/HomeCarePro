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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<TaskModel> another, week, monthly, month3;
    SimpleDateFormat monthFORMAT = new SimpleDateFormat(Constants.monthFORMAT, Locale.getDefault());
    ArrayList<TaskModel> weekStash;
    ArrayList<TaskModel> monthStash;
    ArrayList<TaskModel> month3Stash;

    long holderL = 0;

    ArrayList<TaskModel> list = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);
    ArrayList<TaskModel> newList = new ArrayList<>();
    String date = new SimpleDateFormat(Constants.calFormat, Locale.getDefault()).format(new Date().getTime());
    SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat, Locale.getDefault());
    SimpleDateFormat calformat = new SimpleDateFormat(Constants.calFormat, Locale.getDefault());
    Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        Date date = new Date();
        String d = monthFORMAT.format(date);
        binding.calendarDayText.setText(d);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        long futureTimestamp = calendar.getTimeInMillis();

        another = Stash.getArrayList(Constants.SAVE_LIST, TaskModel.class);

        if (another.isEmpty()){
            another.add(new TaskModel(
                    "29ccc9a4-0c65-4c4f-9ed9-5be0b231c6df",
                    "Important Task",
                    "this is Task description",
                    1726297928048L,
                    "Low",
                    "Weekly",
                    false,
                    "16/09/2024",
                    1726470676437L
            ));

            another.add(new TaskModel(
                    "64315896-f7dc-401e-a9a3-93d47d06d5f3",
                    "another",
                    "Medium",
                    1726297949554L,
                    "Medium",
                    "Monthly",
                    false,
                    "17/09/2024",
                    1726557134537L
            ));

            another.add(new TaskModel(
                    "5277f72e-f8c3-4e8b-8e93-7ca92b0714c8",
                    "eee",
                    "eeee",
                    1726297960111L,
                    "High",
                    "3 Month",
                    false,
                    "16/09/2024",
                    1726470751255L
            ));
            Stash.put(Constants.SAVE_LIST, another);
        }

        week = new ArrayList<>();
        month3 = new ArrayList<>();
        monthly = new ArrayList<>();

        weekStash = Stash.getArrayList(Constants.WEEKLY_LIST, TaskModel.class);
        monthStash = Stash.getArrayList(Constants.MONTHLY_LIST, TaskModel.class);
        month3Stash = Stash.getArrayList(Constants.MONTH3_LIST, TaskModel.class);

        Stash.clear(Constants.WEEKLY_LIST);
        Stash.clear(Constants.MONTHLY_LIST);
        Stash.clear(Constants.MONTH3_LIST);

        showList();

        final CompactCalendarView compactCalendarView = (CompactCalendarView) binding.compactcalendarView;
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        // compactCalendarView.setDayColumnNames(new String[]{"Hel", "Ter", "gu", "ch", "rdt", "fff", "lll"});
        for (int i = 0; i < another.size(); i++) {
            String sDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(another.get(i).getStartingDateTimeStamp());
            int day = Integer.parseInt(sDay);
            String sMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(another.get(i).getStartingDateTimeStamp());
            int month = Integer.parseInt(sMonth);
            String sYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(another.get(i).getStartingDateTimeStamp());
            int year = Integer.parseInt(sYear);

            if(!another.get(i).isStatus()){
                // Check For High Priority Events
                if (another.get(i).getPriority().equals(Constants.HIGH)) {
                    if (another.get(i).getFrequency().equals("Weekly")) {
                        for (int j = 0; j < 52; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);

                            }
                        }
                        Event ev = new Event(getResources().getColor(R.color.high_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                        compactCalendarView.addEvent(ev);
                    }
                    if (another.get(i).getFrequency().equals("Monthly")) {
                        for (int j = 0; j < 12; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);

                            }
                        }
                        Event ev = new Event(getResources().getColor(R.color.high_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                        compactCalendarView.addEvent(ev);
                    }
                    if (another.get(i).getFrequency().equals("3 Month")) {
                        for (int j = 0; j < 12; j = j + 3) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.high_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.HIGH, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);

                            }
                        }
                        Event ev = new Event(getResources().getColor(R.color.high_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                        compactCalendarView.addEvent(ev);
                    }
                }
                // Check For Medium Priority Events
                if (another.get(i).getPriority().equals(Constants.MEDIUM)) {
                    if (another.get(i).getFrequency().equals("Weekly")) {
                        for (int j = 0; j < 52; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);

                            }
                        }
                        Event ev = new Event(getResources().getColor(R.color.medium_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                        compactCalendarView.addEvent(ev);
                    }
                    if (another.get(i).getFrequency().equals("Monthly")) {
                        for (int j = 0; j < 12; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);

                            }
                        }
                        Event ev = new Event(getResources().getColor(R.color.medium_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                        compactCalendarView.addEvent(ev);
                    }
                    if (another.get(i).getFrequency().equals("3 Month")) {
                        for (int j = 0; j < 12; j = j + 3) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);

                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.medium_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.MEDIUM, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);

                            }
                        }
                    }

                }
                // Check For Low Priority Events
                if (another.get(i).getPriority().equals(Constants.LOW)) {
                    if (another.get(i).getFrequency().equals("Weekly")) {

                        for (int j = 0; j < 52; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);


                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(7, ChronoUnit.DAYS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                week.add(taskModel);
                                Stash.put(Constants.WEEKLY_LIST, week);

                            }
                        }
                    }
                    if (another.get(i).getFrequency().equals("Monthly")) {
                        for (int j = 0; j < 12; j++) {
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);


                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(1, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                monthly.add(taskModel);
                                Stash.put(Constants.MONTHLY_LIST, monthly);

                            }
                        }
                    }
                    if (another.get(i).getFrequency().equals("3 Month")) {
                        for (int j = 0; j < 12; j = j + 3) {
                            Log.d("Checking1", "j " + j);
                            LocalDate startDate, endDate;
                            if (j == 0) {
                                startDate = LocalDate.of(year, month, day);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);
                                LocalDateTime dateTime = endDate.atStartOfDay();
                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);
                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);
                            } else {
                                String ssDay = new SimpleDateFormat(Constants.dayFormat, Locale.getDefault()).format(holderL);
                                int dayy = Integer.parseInt(ssDay);
                                String ssMonth = new SimpleDateFormat(Constants.monthFormat, Locale.getDefault()).format(holderL);
                                int monthh = Integer.parseInt(ssMonth);
                                String ssYear = new SimpleDateFormat(Constants.yearFormat, Locale.getDefault()).format(holderL);
                                int yearr = Integer.parseInt(ssYear);

                                startDate = LocalDate.of(yearr, monthh, dayy);
                                endDate = startDate.plus(3, ChronoUnit.MONTHS);

                                LocalDateTime dateTime = endDate.atStartOfDay();

                                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                                holderL = timestamp;
                                Event ev1 = new Event(getResources().getColor(R.color.low_prio), timestamp, another.get(i).getName());
                                compactCalendarView.addEvent(ev1, true);

                                String dt = new SimpleDateFormat(Constants.myFormat, Locale.getDefault()).format(timestamp);
                                TaskModel taskModel = new TaskModel(
                                        another.get(i).getId(),
                                        another.get(i).getName(),
                                        another.get(i).getDescription(),
                                        another.get(i).getDate(),
                                        Constants.LOW, another.get(i).getFrequency(), another.get(i).isStatus(), dt, timestamp
                                );
                                month3.add(taskModel);
                                Stash.put(Constants.MONTH3_LIST, month3);

                            }
                        }
                    }

                    Event ev = new Event(getResources().getColor(R.color.low_prio), another.get(i).getStartingDateTimeStamp(), another.get(i).getName());
                    compactCalendarView.addEvent(ev);
                }
            }

        }
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                String d = new SimpleDateFormat(Constants.calFormat, Locale.getDefault()).format(dateClicked);
                Stash.put(Constants.DATE_CLICK, d);
                startActivity(new Intent(MainActivity.this, DateTaskActivity.class));
                finish();
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date date) {
                String d = monthFORMAT.format(date);
                binding.calendarDayText.setText(d);
                Log.d("DateCh1", "Month was scrolled to: " + date);
            }
        });

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
        try {
            date1 = calformat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < list.size(); i++) {
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

       checkFromWeek();

    }

    private void checkFromWeek() {
        for (int i = 0; i < weekStash.size(); i++) {
            String s = format.format(weekStash.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(weekStash.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        checkFromMonth();
    }

    private void checkFromMonth() {
        for (int i = 0; i < monthStash.size(); i++) {
            String s = format.format(monthStash.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(monthStash.get(i));
            }

            TaskAdapter adapter = new TaskAdapter(this, newList, list);
            binding.recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        checkFrom3Month();
    }

    private void checkFrom3Month() {
        for (int i = 0; i < month3Stash.size(); i++) {
            String s = format.format(month3Stash.get(i).getStartingDateTimeStamp());
            Date date3;
            try {
                date3 = format.parse(s);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date1.compareTo(date3) == 0) {
                newList.add(month3Stash.get(i));
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


}
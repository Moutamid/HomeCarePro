package com.moutamid.homecarepro.caleder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.ViewContainer;
import com.moutamid.homecarepro.R;

public class DayViewContainer extends ViewContainer {
    TextView text;
    public DayViewContainer(@NonNull View textView) {
        super(textView);
        text = (TextView) textView.findViewById(R.id.calendarDayText);
    }

}

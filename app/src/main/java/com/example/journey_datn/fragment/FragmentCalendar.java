//package com.example.journey_datn.fragment;
//
//import androidx.fragment.app.Fragment;
//
//public class FragmentCalendar extends Fragment {
//
//}
package com.example.journey_datn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
public class FragmentCalendar extends Fragment {
    private EntityRepository entityRepository;
    private  List<Entity> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar,container,false);
        CalendarView calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        entityRepository = new EntityRepository(getContext());
        list = entityRepository.getEntity();

//        calendarView.setOnForwardPageChangeListener(() ->
//                Toast.makeText(getActivity(), "Forward", Toast.LENGTH_SHORT).show());
//
//        calendarView.setOnPreviousPageChangeListener(() ->
//                Toast.makeText(getActivity(), "Previous", Toast.LENGTH_SHORT).show());
        /*danh dau o chon*/
//        calendarView.setSelectedDates(getSelectedDays());

        List<EventDay> events = new ArrayList<>();
        List<Calendar> arrCr = getSelectedDays();
        for(int i = 0;i<arrCr.size();i++){
            events.add(new EventDay(arrCr.get(i), null,R.drawable.ic_happy_black_24dp));

        }
        calendarView.setEvents(events);
//gioi han so nam
//
//        Calendar min = Calendar.getInstance();
//        min.add(Calendar.MONTH, -2);
//
//        Calendar max = Calendar.getInstance();
//        max.add(Calendar.MONTH, 2);
//
//        calendarView.setMinimumDate(min);
//        calendarView.setMaximumDate(max);

        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Toast.makeText(getActivity(),
                        eventDay.getCalendar().getTime().toString() + " "
                                + eventDay.isEnabled(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }
    private List<Calendar> getSelectedDays() {
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //Tao ca ngay co su kien de add vao danh sach
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, list.get(i).getDay());
            calendar.set(Calendar.MONTH,list.get(i).getMonth()-1);
            calendar.set(Calendar.YEAR, list.get(i).getYear());
            calendars.add(calendar);
        }
        return calendars;
    }
}
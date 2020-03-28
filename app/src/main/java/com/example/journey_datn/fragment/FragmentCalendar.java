//package com.example.journey_datn.fragment;
//
//import androidx.fragment.app.Fragment;
//
//public class FragmentCalendar extends Fragment {
//
//}
package com.example.journey_datn.fragment;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
public class FragmentCalendar extends Fragment {
    private EntityRepository entityRepository;
    private  ArrayList<Entity> list = new ArrayList<>();
    private  ArrayList<Entity> listItem = new ArrayList<>();
    private RecyclerView rcvCalendar;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private AdapterRcvEntity adapterRcvEntity;
    private  int day, month, year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar,container,false);
        CalendarView calendarView =  v.findViewById(R.id.calendarView);
        entityRepository = new EntityRepository(getContext());
        rcvCalendar = v.findViewById(R.id.rcvCalendar);
        list = (ArrayList<Entity>) entityRepository.getEntity();

        List<EventDay> events = new ArrayList<>();
        List<Calendar> arrCr = getSelectedDays();
        for(int i = 0;i<arrCr.size();i++){
            events.add(new EventDay(arrCr.get(i), null,R.drawable.night_sky));
        }
        calendarView.setEvents(events);
        getCalendar();
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Date date = eventDay.getCalendar().getTime();
                String strDay          = (String) DateFormat.format("dd",   date);
                String strMonthNumber  = (String) DateFormat.format("MM",   date);
                String strYear         = (String) DateFormat.format("yyyy", date);
                 day = Integer.parseInt(strDay);
                 month = Integer.parseInt(strMonthNumber);
                 year = Integer.parseInt(strYear);

                listItem = (ArrayList<Entity>) entityRepository.getEntityByTime(day, month, year);
                adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
                rcvCalendar.setAdapter(adapterRcvEntity);
                rcvCalendar.setLayoutManager(linearLayoutManager);
            }
        });

        return v;
    }

    private void getCalendar() {
        int mYear, mMonth, mDay;
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mMonth  = mMonth + 1;
        listItem = (ArrayList<Entity>) entityRepository.getEntityByTime(mDay, mMonth, mYear);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
        rcvCalendar.setAdapter(adapterRcvEntity);
        rcvCalendar.setLayoutManager(linearLayoutManager);
    }
    private List<Calendar> getSelectedDays() {
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, list.get(i).getDay());
            calendar.set(Calendar.MONTH,list.get(i).getMonth()-1);
            calendar.set(Calendar.YEAR, list.get(i).getYear());
            calendars.add(calendar);
        }
        return calendars;
    }
}
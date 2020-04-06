//package com.example.journey_datn.fragment;
//
//import androidx.fragment.app.Fragment;
//
//public class FragmentCalendar extends Fragment {
//
//}
package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FragmentCalendar extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener {
    private EntityRepository entityRepository;
    private ArrayList<Entity> list = new ArrayList<>();
    private ArrayList<Entity> listItem = new ArrayList<>();
    private RecyclerView rcvCalendar;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private AdapterRcvEntity adapterRcvEntity;
    private int day, month, year, pos;
    private int REQUEST_CODE1 = 10, REQUEST_CODE2 = 20;
    private FloatingActionButton fabCalendar;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = v.findViewById(R.id.calendarView);
        entityRepository = new EntityRepository(getContext());
        rcvCalendar = v.findViewById(R.id.rcvCalendar);
        fabCalendar = v.findViewById(R.id.fab_calendar);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });

        loadDataCalendar();

        return v;
    }

    private void loadDataCalendar() {
        list = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
        List<EventDay> events = new ArrayList<>();
        List<Calendar> arrCr = getSelectedDays();
        for (int i = 0; i < arrCr.size(); i++) {
            events.add(new EventDay(arrCr.get(i), null, R.drawable.night_sky));
        }
        calendarView.setEvents(events);
        getCalendar();
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Date date = eventDay.getCalendar().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String strDate[] = dateFormat.format(date).split("-");
                int  mDay = Integer.parseInt(strDate[0]);
                int mMonth = Integer.parseInt(strDate[1]);
                int mYear = Integer.parseInt(strDate[2]);
                Log.d("aaa", Integer.parseInt(strDate[0]) + " " +  Integer.parseInt(strDate[1]) + " " + strDate[2] + " date: " + dateFormat.format(date));
                setRcvCalendar(mDay, mMonth, mYear);
            }
        });

        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
    }

    private void setRcvCalendar(int day, int month, int year) {
        listItem = (ArrayList<Entity>) entityRepository.getEntityByTime(day, month, year, MainActivity.userId);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
        rcvCalendar.setAdapter(adapterRcvEntity);
        rcvCalendar.setLayoutManager(linearLayoutManager);
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1 && resultCode == ItemDetailActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
        if (requestCode == REQUEST_CODE2 && resultCode == AddDataActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.insertEntity(entity);
            adapterRcvEntity.addData(entity);
        }

        list = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
        List<EventDay> events = new ArrayList<>();
        List<Calendar> arrCr = getSelectedDays();
        for (int i = 0; i < arrCr.size(); i++) {
            events.add(new EventDay(arrCr.get(i), null, R.drawable.night_sky));
        }
        calendarView.setEvents(events);

        setRcvCalendar(day, month, year);
    }

    private void getCalendar() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate[] = format1.format(date).split("-");
         day = Integer.parseInt(strDate[0]);
         month = Integer.parseInt(strDate[1]);
         year = Integer.parseInt(strDate[2]);
        listItem = (ArrayList<Entity>) entityRepository.getEntityByTime(day, month, year, MainActivity.userId);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
        rcvCalendar.setAdapter(adapterRcvEntity);
        rcvCalendar.setLayoutManager(linearLayoutManager);
    }

    private List<Calendar> getSelectedDays() {
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            String strDate[] = list.get(i).getStrDate().split("-");
            String strYear[] = strDate[2].split(" ");
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(strDate[1]) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(strYear[0]));
            calendars.add(calendar);
        }
        return calendars;
    }

    @Override
    public void onItemClick(int position) {
        pos = position;
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("entity", listItem.get(position));
        intent.putParcelableArrayListExtra("listEntity", listItem);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE1);

    }

    @Override
    public void onItemLongClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this journal entry?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                entityRepository.deleteEntity(adapterRcvEntity.getLstEntity().get(position));
                adapterRcvEntity.getLstEntity().remove(position);
                adapterRcvEntity.notifyItemRemoved(position);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
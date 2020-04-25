package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentCalendar extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener,
        AdapterRcvEntity.onCountItemListener{

    private ArrayList<Entity> listEntity;
    private ArrayList<Entity> listItem = new ArrayList<>();
    private RecyclerView rcvCalendar;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private AdapterRcvEntity adapterRcvEntity;
    private int day, month, year, pos;
    private int REQUEST_CODE1 = 10, REQUEST_CODE2 = 20;
    private FloatingActionButton fabCalendar;
    private CalendarView calendarView;
    private FirebaseDB firebaseDB = new FirebaseDB(MainActivity.userId);
    private Set<Integer> posDelete = new HashSet<>();
    private onDataChangeListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        listEntity = MainActivity.entityList;
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = v.findViewById(R.id.calendarView);
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
                int mDay = Integer.parseInt(strDate[0]);
                int mMonth = Integer.parseInt(strDate[1]);
                int mYear = Integer.parseInt(strDate[2]);
                setRcvCalendar(mDay, mMonth, mYear);
            }
        });

        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
        adapterRcvEntity.setOnCountItemListener(this);
    }

    private void setRcvCalendar(int day, int month, int year) {
        listItem = (ArrayList<Entity>) firebaseDB.getEntityByTime(listEntity,day, month, year);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
        rcvCalendar.setAdapter(adapterRcvEntity);
        rcvCalendar.setLayoutManager(linearLayoutManager);
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
        adapterRcvEntity.setOnCountItemListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1 && resultCode == ItemDetailActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            pos = data.getIntExtra("position", -1);
            firebaseDB.updateEntity(entity.getId(), entity);
            adapterRcvEntity.setData(entity, pos);
        }
        if (requestCode == REQUEST_CODE2 && resultCode == AddDataActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            firebaseDB.insertEntity(entity);
            listener.onDataChange(true);
        }

    }

    private void getCalendar() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate[] = format1.format(date).split("-");
        day = Integer.parseInt(strDate[0]);
        month = Integer.parseInt(strDate[1]);
        year = Integer.parseInt(strDate[2]);
        listItem = (ArrayList<Entity>) firebaseDB.getEntityByTime(listEntity, day, month, year);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listItem);
        rcvCalendar.setAdapter(adapterRcvEntity);
        rcvCalendar.setLayoutManager(linearLayoutManager);
    }

    private List<Calendar> getSelectedDays() {
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < listEntity.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, listEntity.get(i).getDay());
            calendar.set(Calendar.MONTH, listEntity.get(i).getMonth() - 1);
            calendar.set(Calendar.YEAR, listEntity.get(i).getYear());
            calendars.add(calendar);
        }
        return calendars;
    }

    @Override
    public void onItemClick(int position) {
        posDelete.add(position);
        pos = position;
        day = listItem.get(position).getDay();
        month = listItem.get(position).getMonth();
        year = listItem.get(position).getYear();
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
                adapterRcvEntity.notifiData(posDelete);
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseDB.deleteEntity(adapterRcvEntity.getLstEntity().get(position).getId());
                adapterRcvEntity.getLstEntity().remove(position);
                adapterRcvEntity.notifyItemRemoved(position);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCountItem(int count) {
    }

    public void setOnDataChangeListener(onDataChangeListener listener){
        this.listener = listener;
    }

    public interface onDataChangeListener{
        void onDataChange(boolean change);
    }
}
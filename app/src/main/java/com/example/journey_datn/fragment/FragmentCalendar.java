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
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
public class FragmentCalendar extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener{
    private EntityRepository entityRepository;
    private  ArrayList<Entity> listItem = new ArrayList<>();
    private RecyclerView rcvCalendar;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private AdapterRcvEntity adapterRcvEntity;
    private  int day, month, year, pos;
    private int REQUEST_CODE1 = 10, REQUEST_CODE2 = 20;
    private FloatingActionButton fabCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar,container,false);
        CalendarView calendarView =  v.findViewById(R.id.calendarView);
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
//        listItem =  (ArrayList<Entity>) entityRepository.getEntityByTime(day, month, year);
        List<EventDay> events = new ArrayList<>();
        List<Calendar> arrCr = getSelectedDays();
        for(int i = 0;i<arrCr.size();i++){
            events.add(new EventDay(arrCr.get(i), null,R.color.colorPrimary));
        }
        calendarView.setEvents(events);
        getCalendar();
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
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
                adapterRcvEntity.loadData(listItem);
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
        for (int i = 0; i < listItem.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, listItem.get(i).getDay());
            calendar.set(Calendar.MONTH,listItem.get(i).getMonth()-1);
            calendar.set(Calendar.YEAR, listItem.get(i).getYear());
            calendars.add(calendar);
        }
        return calendars;
    }

    @Override
    public void onItemClick(int position) {
        itemClick(position);

    }

    @Override
    public void onItemLongClick(int position) {
        itemLongClick(position);

    }

    private void itemClick(int position){
        pos = position;
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("entity",  listItem.get(position));
        intent.putParcelableArrayListExtra("listEntity", listItem);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE1);
    }

    private void itemLongClick(int position){
        showDialog(position);
    }

    private void showDialog(final int position){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1 && resultCode == ItemDetailActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
        if (requestCode == REQUEST_CODE2 && resultCode == AddDataActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.insertEntity(entity);
            adapterRcvEntity.addData(entity);
        }
    }
}
package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.List;

public class FragmentJourney extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener{

    private RecyclerView rcvJourney;
    private AdapterRcvEntity adapterRcvEntity;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FloatingActionButton fabJourney;
    private TextView txt_user_name, txt_day, txt_month, txt_year, txt_number_item, txtDayOfWeek;
    private int REQUEST_CODE = 911;
    private int pos;

    private ArrayList<Entity> lstEntity;

    private EntityRepository entityRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey, container, false);
        initView(view);
        getCalendar();
        fabJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                intent.putExtra("activity", 1);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        entityRepository = new EntityRepository(getContext());

        lstEntity = (ArrayList<Entity>) entityRepository.getEntity();
        adapterRcvEntity = new AdapterRcvEntity(getContext(), lstEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);
        txt_number_item.setText("" + adapterRcvEntity.getItemCount());

        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == AddDataActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.insertEntity(entity);
            adapterRcvEntity.addData(entity);
            txt_number_item.setText("" + adapterRcvEntity.getItemCount());
        }
        if (requestCode == REQUEST_CODE && resultCode == ItemDetailActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
    }

    private void initView(View view) {
        rcvJourney = view.findViewById(R.id.rcv_journey);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_day = view.findViewById(R.id.txt_day);
        txt_month = view.findViewById(R.id.txt_month);
        txt_year = view.findViewById(R.id.txt_year);
        txt_number_item = view.findViewById(R.id.txt_number_item);
        fabJourney = view.findViewById(R.id.fab_journey);
        txtDayOfWeek = view.findViewById(R.id.txt_dayOfWeek);
    }

    private void getCalendar() {
        int mYear, mMonth, mDay;
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        getDayofMonth(mDay, mMonth, mYear);
        mMonth = mMonth + 1;
        txt_day.setText(mDay + "");
        txt_month.setText(mMonth + "");
        txt_year.setText(mYear + "");
    }
    private void getDayofMonth(int day, int month, int year) {
        day = day - 1;
        Date date = new Date(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                txtDayOfWeek.setText("Sunday");
                break;
            case Calendar.MONDAY:
                txtDayOfWeek.setText("Monday");
                break;
            case Calendar.TUESDAY:
                txtDayOfWeek.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                txtDayOfWeek.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                txtDayOfWeek.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                txtDayOfWeek.setText("Friday");
                break;
            case Calendar.SATURDAY:
                txtDayOfWeek.setText("Saturday");
                break;
        }
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
        intent.putExtra("entity",  lstEntity.get(position));
        intent.putParcelableArrayListExtra("listEntity", lstEntity);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void itemLongClick(final int pos){
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
                entityRepository.deleteEntity(adapterRcvEntity.getLstEntity().get(pos));
                adapterRcvEntity.getLstEntity().remove(pos);
                adapterRcvEntity.notifyItemRemoved(pos);
                txt_number_item.setText("" + adapterRcvEntity.getItemCount());
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

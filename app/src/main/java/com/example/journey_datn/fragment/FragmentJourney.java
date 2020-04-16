package com.example.journey_datn.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentJourney extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener {

    private RecyclerView rcvJourney;
    private AdapterRcvEntity adapterRcvEntity;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FloatingActionButton fabJourney;
    private TextView txt_user_name, txtDate, txt_number_item, txtDayOfWeek;
    private int REQUEST_CODE = 911;
    private int pos;
    private Set<Integer> posDelete = new HashSet<>();
    private ImageView imgDelete;

    private ArrayList<Entity> lstEntity;
    private EntityRepository entityRepository;
    private boolean checkRdb = false;

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

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
            }
        });

        entityRepository = new EntityRepository(getContext());

        lstEntity = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
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
        if (requestCode == REQUEST_CODE && resultCode == ItemDetailActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
    }

    private void initView(View view) {
        rcvJourney = view.findViewById(R.id.rcv_journey);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txtDate = view.findViewById(R.id.txt_date);
        txt_number_item = view.findViewById(R.id.txt_number_item);
        fabJourney = view.findViewById(R.id.fab_journey);
        txtDayOfWeek = view.findViewById(R.id.txt_dayOfWeek);
        String firstName = MainActivity.firstName;
        String lastName = MainActivity.lastName;
        txt_user_name.setText(lastName + " " + firstName);
        imgDelete = view.findViewById(R.id.img_delete_item);
    }

    private void getCalendar() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String today = format1.format(date);
        txtDate.setText(today);
        String arrToDay[] = today.split("-");
        int mDay = Integer.parseInt(arrToDay[0]);
        int mMonth = Integer.parseInt(arrToDay[1]);
        int mYear = Integer.parseInt(arrToDay[2]);
        getDayofMonth(mDay, mMonth, mYear);

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
        if (checkRdb) {
            if (lstEntity.get(position).isCheckRdb())
                posDelete.add(position);
            else if (posDelete.contains(position))
                posDelete.remove(position);
        } else {
            pos = position;
            Intent intent = new Intent(getContext(), ItemDetailActivity.class);
            intent.putExtra("entity", lstEntity.get(position));
            intent.putParcelableArrayListExtra("listEntity", lstEntity);
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemLongClick(int position) {
        checkRdb = true;
        imgDelete.setVisibility(View.VISIBLE);
        fabJourney.setVisibility(View.INVISIBLE);
    }

    private void deleteItems() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete " + posDelete.size() + " journal entry?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refreshDelete();
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<Entity> entities = new ArrayList<>();
                lstEntity = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
                for (int element : posDelete)
                    entities.add(lstEntity.get(element));
                adapterRcvEntity.removeData(posDelete);
                for (Entity entity : entities)
                    entityRepository.deleteEntity(entity);
                txt_number_item.setText("" + adapterRcvEntity.getItemCount());

                refreshData();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("RestrictedApi")
    private void refreshDelete() {
        checkRdb = false;
        imgDelete.setVisibility(View.INVISIBLE);
        fabJourney.setVisibility(View.VISIBLE);
        adapterRcvEntity.notifiData(posDelete);
        posDelete = new HashSet<>();
    }

    @SuppressLint("RestrictedApi")
    private void refreshData(){
        entityRepository = new EntityRepository(getContext());
        lstEntity = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
        adapterRcvEntity = new AdapterRcvEntity(getContext(), lstEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);
        txt_number_item.setText("" + adapterRcvEntity.getItemCount());
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);

        checkRdb = false;
        imgDelete.setVisibility(View.INVISIBLE);
        fabJourney.setVisibility(View.VISIBLE);
        posDelete = new HashSet<>();
    }
}

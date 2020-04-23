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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.BranchDBFB;
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentJourney extends Fragment implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener,
        AdapterRcvEntity.onCountItemListener {

    private RecyclerView rcvJourney;
    private AdapterRcvEntity adapterRcvEntity;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FloatingActionButton fabJourney;
    private TextView txt_user_name, txtDate, txt_number_item, txtDayOfWeek;
    private int REQUEST_CODE = 911;
    private int pos;
    private Set<Integer> posDelete = new HashSet<>();
    private ImageView imgDelete;

    private ArrayList<Entity> listEntity;
    private FirebaseDB firebaseDB = new FirebaseDB(MainActivity.userId);
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

        listEntity = MainActivity.entityList;
        adapterRcvEntity = new AdapterRcvEntity(getContext(), listEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
        adapterRcvEntity.setOnCountItemListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == AddDataActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            firebaseDB.insertEntity(entity);
        }
        if (requestCode == REQUEST_CODE && resultCode == ItemDetailActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            firebaseDB.updateEntity(entity.getId(), entity);
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
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String today = format1.format(date);
        txtDate.setText(today);
        getDayofMonth(calendar);
    }

    private void getDayofMonth(Calendar calendar) {
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
            if (listEntity.get(position).isCheckRdb())
                posDelete.add(position);
            else if (posDelete.contains(position))
                posDelete.remove(position);
        } else {
            pos = position;
            Intent intent = new Intent(getContext(), ItemDetailActivity.class);
            intent.putExtra("entity", listEntity.get(position));
            intent.putParcelableArrayListExtra("listEntity", listEntity);
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

    @Override
    public void onCountItem(int count) {
        txt_number_item.setText("" + count);
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
                for (int element : posDelete)
                    entities.add(listEntity.get(element));
                for (Entity entity : entities)
                    firebaseDB.deleteEntity(entity.getId());

                refreshDelete();
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
        loadFragment(new FragmentJourney());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_contain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

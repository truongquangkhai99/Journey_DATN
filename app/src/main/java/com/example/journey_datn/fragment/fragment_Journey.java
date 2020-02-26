package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.journey_datn.Adapter.AdapterRcvEmpty;
import com.example.journey_datn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class fragment_Journey extends Fragment {

    private RecyclerView rcvJourney;
    private AdapterRcvEmpty adapterRcvEmpty;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private FloatingActionButton fabJourney;
    private TextView txt_user_name, txt_day,txt_month, txt_year, txt_number_item;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey, container, false);
        initView(view);
        fabJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivity(intent);
            }
        });

        adapterRcvEmpty = new AdapterRcvEmpty(getContext());
        rcvJourney.setAdapter(adapterRcvEmpty);
        rcvJourney.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void initView(View view) {
        rcvJourney = view.findViewById(R.id.rcv_journey);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_day = view.findViewById(R.id.txt_day);
        txt_month = view.findViewById(R.id.txt_month);
        txt_year = view.findViewById(R.id.txt_year);
        txt_number_item = view.findViewById(R.id.txt_number_item);
        fabJourney = view.findViewById(R.id.fab_journey);
    }
}

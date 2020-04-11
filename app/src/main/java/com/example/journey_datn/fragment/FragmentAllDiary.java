package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.DairyActivity;
import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvAllDiary;
import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.R;
import com.example.journey_datn.db.DiaryRepository;

import java.util.ArrayList;

public class FragmentAllDiary extends Fragment implements AdapterRcvAllDiary.onItemLongClickListener{
    private RecyclerView rcvAllDiary;
    private AdapterRcvAllDiary adapterRcvAllDiary;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private DiaryRepository diaryRepository;
    private ArrayList<Diary> list = new ArrayList<>();
    private AdapterRcvAllDiary.OnClickItemTab1 onClickItemTab1;
    private ImageView imgBack;

    public void setOnClickItemTab1(AdapterRcvAllDiary.OnClickItemTab1 onClickItemTab1) {
        this.onClickItemTab1 = onClickItemTab1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_diary, container, false);
        rcvAllDiary = view.findViewById(R.id.rcv_all_diary);
        imgBack = view.findViewById(R.id.img_back_all_diary);
        getDataDelay();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
    private  void getDataDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                diaryRepository = new DiaryRepository(getContext());
                list = (ArrayList<Diary>) diaryRepository.getDiary(MainActivity.userId);
                adapterRcvAllDiary = new AdapterRcvAllDiary(getContext(), list, onClickItemTab1);
                rcvAllDiary.setAdapter(adapterRcvAllDiary);
                rcvAllDiary.setLayoutManager(linearLayoutManager);
                setAdapter();
            }

        }, 300);
    }

    private void setAdapter(){
        adapterRcvAllDiary.setItemLongClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getDataDelay();
    }

    @Override
    public void onItemLongClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this diary?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                diaryRepository.deleteDiary(adapterRcvAllDiary.getListDiary().get(position));
                adapterRcvAllDiary.getListDiary().remove(position);
                adapterRcvAllDiary.notifyItemRemoved(position);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

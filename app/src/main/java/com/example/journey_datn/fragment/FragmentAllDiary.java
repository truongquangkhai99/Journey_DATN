package com.example.journey_datn.fragment;

import android.annotation.SuppressLint;
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
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvAllDiary;
import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.R;
import com.example.journey_datn.db.DiaryRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentAllDiary extends Fragment implements AdapterRcvAllDiary.onItemLongClickListener, AdapterRcvAllDiary.onItemClickListener {
    private RecyclerView rcvAllDiary;
    private AdapterRcvAllDiary adapterRcvAllDiary;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private DiaryRepository diaryRepository;
    private ArrayList<Diary> list = new ArrayList<>();
    private AdapterRcvAllDiary.OnClickItemTab1 onClickItemTab1;
    private ImageView imgBack;

    private Set<Integer> posDelete = new HashSet<>();
    private boolean checkRdb = false;
    private ImageView imgDelete;

    public void setOnClickItemTab1(AdapterRcvAllDiary.OnClickItemTab1 onClickItemTab1) {
        this.onClickItemTab1 = onClickItemTab1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_diary, container, false);
        rcvAllDiary = view.findViewById(R.id.rcv_all_diary);
        imgDelete = view.findViewById(R.id.img_delete_all_diary);
        imgBack = view.findViewById(R.id.img_back_all_diary);
        getDataDelay();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
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

        }, 200);
    }

    private void setAdapter(){
        adapterRcvAllDiary.setItemLongClickListener(this);
        adapterRcvAllDiary.setItemClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getDataDelay();
    }

    @Override
    public void onItemClick(int position) {
        if (checkRdb) {
            if (list.get(position).isCheckRdb())
                posDelete.add(position);
            else if (posDelete.contains(position))
                posDelete.remove(position);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        checkRdb = true;
        imgDelete.setVisibility(View.VISIBLE);
    }

    private void deleteItems() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete " + posDelete.size() + " diary?");
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
                List<Diary> diaries = new ArrayList<>();
                list = (ArrayList<Diary>) diaryRepository.getDiary(MainActivity.userId);
                for (int element : posDelete)
                    diaries.add(list.get(element));
                adapterRcvAllDiary.removeData(posDelete);
                for (Diary diary : diaries)
                    diaryRepository.deleteDiary(diary);
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
        adapterRcvAllDiary.notifiData(posDelete);
        posDelete = new HashSet<>();
    }

    @SuppressLint("RestrictedApi")
    private void refreshData(){
        diaryRepository = new DiaryRepository(getContext());
        list = (ArrayList<Diary>) diaryRepository.getDiary(MainActivity.userId);
        adapterRcvAllDiary = new AdapterRcvAllDiary(getContext(), list, onClickItemTab1);
        rcvAllDiary.setAdapter(adapterRcvAllDiary);
        rcvAllDiary.setLayoutManager(linearLayoutManager);
        setAdapter();

        checkRdb = false;
        imgDelete.setVisibility(View.INVISIBLE);
        posDelete = new HashSet<>();
    }
}

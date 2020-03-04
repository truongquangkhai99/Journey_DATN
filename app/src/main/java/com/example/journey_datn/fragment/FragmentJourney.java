package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentJourney extends Fragment  implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private RecyclerView rcvJourney;
    private AdapterRcvEntity adapterRcvEntity;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FloatingActionButton fabJourney;
    private TextView txt_user_name, txt_day,txt_month, txt_year, txt_number_item;

    private ArrayList<Entity> lstEntity;

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
                startActivityForResult(intent, 911);
            }
        });

        lstEntity = new ArrayList<>();
        adapterRcvEntity = new AdapterRcvEntity(getContext(), lstEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);

        adapterRcvEntity.getData();

//        adapterRcvEntity.deleteAllEntity();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 911 && resultCode == 113){
            Entity entity = data.getParcelableExtra("entity");
            adapterRcvEntity.insertEntity(entity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterRcvEntity.mCompositeDisposable.clear();
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

    private void getCalendar(){
        int mYear, mMonth, mDay;
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = mMonth + 1;
        txt_day.setText(mDay + "");
        txt_month.setText(mMonth + "");
        txt_year.setText(mYear + "");

    }

    @Override
    public void onClick(View v) {

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
//                Entity user = new Entity("Toan", " Doan " + new Random().nextInt());
//                Place place = new Place("YB " + new Random().nextInt());
//                user.setPlace(place);
//                mUserRepository.insertUser(user);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //no ops
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        adapterRcvEntity.onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        adapterRcvEntity.getData();
                    }
                });

        adapterRcvEntity.mCompositeDisposable.add(disposable);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Entity entity = lstEntity.get(position);
        final EditText editText = new EditText(getContext());
//        editText.setText(entity.getLastName());
//        editText.setHint(R.string.hint_last_name);
        new AlertDialog.Builder(getContext()).setTitle("Edit")
                .setMessage("Edit user last name")
                .setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            return;
                        }
//                        entity.setLastName(editText.getText().toString());
                        adapterRcvEntity.updateEntity(entity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Entity entity = lstEntity.get(position);
        new AlertDialog.Builder(getContext()).setTitle("Delete")
                .setMessage("Do you want to delete " + entity.toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapterRcvEntity.deleteEntity(entity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
        return false;
    }
}

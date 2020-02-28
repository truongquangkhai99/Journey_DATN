package com.example.journey_datn.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityDatabase;
import com.example.journey_datn.db.EntityLocalDataSource;
import com.example.journey_datn.db.EntityRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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


    private CompositeDisposable mCompositeDisposable;
    private EntityRepository mEntityRepository;
    private List<Entity> mEntity;

    private String contain,day,month,year,hour,minute,action,position,mood,srcImage;
    private int temperature, th;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey, container, false);
        initView(view);
        fabJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivityForResult(intent, 911);
            }
        });

        mEntity = new ArrayList<>();
        adapterRcvEntity = new AdapterRcvEntity(getContext(), mEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);

        mCompositeDisposable = new CompositeDisposable();


//        rcvJourney.setOnItemLongClickListener(this);
//        rcvJourney.setOnItemClickListener(this);

        EntityDatabase entityDatabase = EntityDatabase.getInMemoryDatabase(getContext());
        mEntityRepository = EntityRepository.getInstance(EntityLocalDataSource.getInstance(entityDatabase.EntityDao()));

        getData();
        //

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 911 && resultCode == 113){
           Bundle bundle = data.getBundleExtra("data");

            contain = bundle.getString("contain");
            day = bundle.getString("day");
            month = bundle.getString("month");
            year = bundle.getString("year");
            hour = bundle.getString("hour");
            minute = bundle.getString("minute");
            action = bundle.getString("action");
            position = bundle.getString("position");
            mood = bundle.getString("mood");
            srcImage = bundle.getString("srcImage");
            temperature = bundle.getInt("temperature");
            th = bundle.getInt("th");

            Entity entity = new Entity();
            entity.setContent(contain);
            entity.setDay(Integer.parseInt(day));
            entity.setMonth(Integer.parseInt(month));
            entity.setYear(Integer.parseInt(year));
            entity.setHour(Integer.parseInt(hour));
            entity.setMinute(Integer.parseInt(minute));
            entity.setAction(action);
            entity.setStrPosition(position);
            entity.setMood(mood);
            entity.setSrcImage(srcImage);
            entity.setTemperature(temperature);
            entity.setTh(th);

            insertEntity(entity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void getData() {
        Disposable disposable = mEntityRepository.getALlEntity()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Entity>>() {
                    @Override
                    public void accept(List<Entity> entities) throws Exception {
                        onGetAllUserSuccess(entities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onGetAllUserFailure(throwable.getMessage());
                    }
                });

        mCompositeDisposable.add(disposable);

    }

    private void onGetAllUserFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onGetAllUserSuccess(List<Entity> entities) {
        mEntity.clear();
        mEntity.addAll(entities);
        adapterRcvEntity.notifyDataSetChanged();
    }



    private void deleteAllUser() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mEntityRepository.deleteAllEntity();
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
                        onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getData();
                    }
                });

        mCompositeDisposable.add(disposable);
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
                        onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getData();
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Entity entity = mEntity.get(position);
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
                        updateEntity(entity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    public void updateEntity(final Entity entity) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mEntityRepository.updateEntity(entity);
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
                        onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getData();
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Entity entity = mEntity.get(position);
        new AlertDialog.Builder(getContext()).setTitle("Delete")
                .setMessage("Do you want to delete " + entity.toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEntity(entity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
        return false;
    }

    private void deleteEntity(final Entity entity) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mEntityRepository.deleteEntity(entity);
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
                        onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getData();
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    private void insertEntity(final Entity entity){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mEntityRepository.insertEntity(entity);
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
                        onGetAllUserFailure(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        getData();
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}

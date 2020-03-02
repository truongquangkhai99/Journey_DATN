package com.example.journey_datn.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityDatabase;
import com.example.journey_datn.db.EntityLocalDataSource;
import com.example.journey_datn.db.EntityRepository;

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

public class AdapterRcvEntity extends RecyclerView.Adapter<AdapterRcvEntity.ViewHolder> {
    private Context context;
    private List<Entity> lstEntity;
    private OnItemClickListener listener;

    public CompositeDisposable mCompositeDisposable;
    public EntityRepository mEntityRepository;


    public AdapterRcvEntity(Context context, List<Entity> mEntity) {
        this.context = context;
        this.lstEntity = mEntity;

        mCompositeDisposable = new CompositeDisposable();
        EntityDatabase entityDatabase = EntityDatabase.getInMemoryDatabase(context);
        mEntityRepository = EntityRepository.getInstance(EntityLocalDataSource.getInstance(entityDatabase.EntityDao()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Entity pos = lstEntity.get(position);
        holder.txt_contain_item.setText(pos.getContent());
        holder.txt_day_item.setText(pos.getDay() + "");
        holder.txt_hour_item.setText(pos.getHour() + "");
        holder.txt_minute_item.setText(pos.getMinute() + "");
        holder.txt_month_item.setText(pos.getMonth() + "");
        holder.txt_position_item.setText(pos.getStrPosition());
        holder.txt_temperature_item.setText(pos.getTemperature() + "");
        holder.txt_th.setText(pos.getTh() + "");
        holder.txt_year_item.setText(pos.getYear() + "");

//        holder.img_mood.setImageResource(pos.getMood());
//        holder.img_action.setImageResource(pos.getAction());
//        holder.img_item.setImageURI(Uri.parse(pos.getSrcImage()));
        Glide.with(context).load(pos.getMood()).into(holder.img_mood);
        Glide.with(context).load(pos.getAction()).into(holder.img_action);

        Glide.with(context).load(Uri.parse(pos.getSrcImage())).into(holder.img_item);

        holder.const_item_layout_rcv.setOnClickListener(new View.OnClickListener() {
            int mpositon = position;
            @Override
            public void onClick(View v) {
                itemClick(mpositon);
            }
        });

        holder.const_item_layout_rcv.setOnLongClickListener(new View.OnLongClickListener() {
            int mposition = position;
            @Override
            public boolean onLongClick(View v) {
                itemLongClick(mposition);
                return false;
            }
        });
    }

    public static Bitmap convertStringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void itemClick(int mpositon){
         String content;
         String strPosition, th;
         int temperature, action, mood;
         int year, month, day, hour, minute;
         String srcImage;

         content = lstEntity.get(mpositon).getContent();
         action = lstEntity.get(mpositon).getAction();
         strPosition = lstEntity.get(mpositon).getStrPosition();
         mood = lstEntity.get(mpositon).getMood();
         srcImage = lstEntity.get(mpositon).getSrcImage();
         temperature = lstEntity.get(mpositon).getTemperature();
         year = lstEntity.get(mpositon).getYear();
         month = lstEntity.get(mpositon).getMonth();
         day = lstEntity.get(mpositon).getDay();
         th = lstEntity.get(mpositon).getTh();
         hour = lstEntity.get(mpositon).getHour();
         minute = lstEntity.get(mpositon).getMinute();

        Intent intent = new Intent(context, ItemDetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("content", content);
        bundle.putInt("action", action);
        bundle.putString("strPosition", strPosition);
        bundle.putInt("mood", mood);
        bundle.putString("srcImage", srcImage);
        bundle.putInt("temperature", temperature);
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        bundle.putString("th", th);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);

        intent.putExtra("bundle", bundle);
        context.startActivity(intent);

    }

    private void itemLongClick(int mposition){
        showDialog(mposition);
    }

    private void showDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                deleteEntity(lstEntity.get(position));
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return lstEntity.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Entity> Entity) {
        if (Entity != null && Entity.size() > 0) {
            lstEntity.clear();
            lstEntity.addAll(Entity);
            notifyDataSetChanged();
        }
    }

    public void addData(List<Entity> Entity) {
        if (Entity != null && Entity.size() > 0) {
            int oldSize = lstEntity.size();
            lstEntity.addAll(Entity);
            notifyItemRangeChanged(oldSize, Entity.size());
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(String source, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_item, img_mood, img_action;
        ConstraintLayout const_item_layout_rcv;
        TextView txt_day_item, txt_month_item, txt_year_item, txt_th, txt_hour_item, txt_minute_item,
                txt_contain_item, txt_position_item, txt_temperature_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.img_item);
            img_mood = itemView.findViewById(R.id.img_icon_happy_item);
            img_action = itemView.findViewById(R.id.img_icon_collect_item);
            txt_day_item = itemView.findViewById(R.id.txt_day_item);
            txt_month_item = itemView.findViewById(R.id.txt_month_item);
            txt_year_item = itemView.findViewById(R.id.txt_year_item);
            txt_th = itemView.findViewById(R.id.txt_th);
            txt_hour_item = itemView.findViewById(R.id.txt_hour_item);
            txt_minute_item = itemView.findViewById(R.id.txt_minute_item);
            txt_contain_item = itemView.findViewById(R.id.txt_contain_item);
            txt_position_item = itemView.findViewById(R.id.txt_position_item);
            txt_temperature_item = itemView.findViewById(R.id.txt_temperature_item);
            const_item_layout_rcv = itemView.findViewById(R.id.const_item_layout_rcv);
        }
    }

    public void getData() {
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

    public void onGetAllUserFailure(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void onGetAllUserSuccess(List<Entity> entities) {
        lstEntity.clear();
        lstEntity.addAll(entities);
        notifyDataSetChanged();
    }



    public void deleteAllEntity() {
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

    public void deleteEntity(final Entity entity) {
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

    public void insertEntity(final Entity entity){
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

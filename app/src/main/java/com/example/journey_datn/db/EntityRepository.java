package com.example.journey_datn.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.journey_datn.Model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EntityRepository {
    private Context context;

    public  EntityRepository(Context context){
        this.context = context;
    }

    interface CallbackReciever {
        public void receiveData(int result);
    }

    public int getItemCount() {

        class getItemEntity extends AsyncTask<Void, Void, Integer> implements CallbackReciever {
            Context context;
            public getItemEntity(Context context){
                this.context = context;
            }
            @Override
            protected Integer doInBackground(Void... voids) {
                int count =  DatabaseClient.getInstance(context).getAppDatabase()
                        .EntityDao()
                        .getCountItem();
                return count;
            }

            @SuppressLint("WrongThread")
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }

            @Override
            public void receiveData(int result) {
            }
        }

        int sl = 0;
        try {
            sl = new getItemEntity(context){
                @Override
                public void receiveData(int result) {
                    super.receiveData(result);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return  sl;
    }


    public List<Entity> getEntity(){
        class GetTasks extends AsyncTask<Void, Void, List<Entity>> implements CallbackReciever {
            Context context;
            public GetTasks(Context context){
                this.context = context;
            }
            @Override
            protected List<Entity> doInBackground(Void... voids) {
                List<Entity> taskList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .EntityDao()
                        .getAllEntity();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Entity> tasks) {
                super.onPostExecute(tasks);
            }

            @Override
            public void receiveData(int result) {

            }
        }

        List<Entity> lst = null;
        try {
            lst = new GetTasks(context){
                @Override
                public void receiveData(int result) {
                    super.receiveData(result);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lst;
    }

    public void insertEntity(final Entity entity){
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //adding to database
                DatabaseClient.getInstance(context).getAppDatabase()
                        .EntityDao()
                        .insertEntity(entity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Toast.makeText(context, "Inserted", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    public void updateEntity(final Entity entity){

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .EntityDao()
                        .updateEntity(entity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    public void deleteEntity(final Entity entity){
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .EntityDao()
                        .deleteEntity(entity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }

    public void deleteAllEntity(){
        class DeleteAllTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .EntityDao()
                        .deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Deleted All", Toast.LENGTH_LONG).show();
            }
        }

        DeleteAllTask dt = new DeleteAllTask();
        dt.execute();
    }


}

package com.example.journey_datn.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.journey_datn.Model.Diary;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiaryRepository {
    private Context context;

    public DiaryRepository(Context context) {
        this.context = context;
    }

    interface CallbackReciever {
        public void receiveData(int result);
    }


    public List<Diary> getDiary(int userId) {
        class GetTasks extends AsyncTask<Void, Void, List<Diary>> implements CallbackReciever {
            Context context;

            public GetTasks(Context context) {
                this.context = context;
            }

            @Override
            protected List<Diary> doInBackground(Void... voids) {
                List<Diary> taskList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .DiaryDao()
                        .getAllDiary(userId);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Diary> tasks) {
                super.onPostExecute(tasks);
            }

            @Override
            public void receiveData(int result) {

            }
        }

        List<Diary> lst = null;
        try {
            lst = new GetTasks(context) {
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

    public void insertDiary(final Diary diary) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .DiaryDao()
                        .insertDiary(diary);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    public void updateDiary(final Diary diary) {

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .DiaryDao()
                        .updateDiary(diary);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    public void deleteDiary(final Diary diary) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .DiaryDao()
                        .deleteDiary(diary);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }
}

package com.example.journey_datn.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.journey_datn.Model.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private Context context;

    public UserRepository(Context context) {
        this.context = context;
    }

    interface CallbackReciever {
        public void receiveData(int result);
    }

    public void insertUser(final User user) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .UserDao()
                        .insertUser(user);
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

    public List<User> getUser() {
        class GetTasks extends AsyncTask<Void, Void, List<User>> implements UserRepository.CallbackReciever {
            Context context;

            public GetTasks(Context context) {
                this.context = context;
            }

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> taskList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .UserDao()
                        .getAllUser();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<User> tasks) {
                super.onPostExecute(tasks);
            }

            @Override
            public void receiveData(int result) {

            }
        }

        List<User> lst = null;
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
}

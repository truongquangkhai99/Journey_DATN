package com.example.journey_datn.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.journey_datn.Model.ImageJounrey;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ImageRepository {
    private Context context;

    public ImageRepository(Context context) {
        this.context = context;
    }

    interface CallbackReciever {
        public void receiveData(int result);
    }

    public int getItemCount() {

        class getItemImage extends AsyncTask<Void, Void, Integer> implements ImageRepository.CallbackReciever {
            Context context;

            public getItemImage(Context context) {
                this.context = context;
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                int count = DatabaseClient.getInstance(context).getImageDatabase()
                        .ImgaeDao()
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
            sl = new getItemImage(context) {
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

        return sl;
    }

    public List<ImageJounrey> getListImages() {
        class GetTasks extends AsyncTask<Void, Void, List<ImageJounrey>> implements CallbackReciever {
            Context context;

            public GetTasks(Context context) {
                this.context = context;
            }

            @Override
            protected List<ImageJounrey> doInBackground(Void... voids) {
                List<ImageJounrey> taskList = DatabaseClient
                        .getInstance(context)
                        .getImageDatabase()
                        .ImgaeDao()
                        .getAllImage();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<ImageJounrey> tasks) {
                super.onPostExecute(tasks);
            }

            @Override
            public void receiveData(int result) {

            }
        }
        List<ImageJounrey> lst = null;
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
//        }
        }
        return lst;
    }
}

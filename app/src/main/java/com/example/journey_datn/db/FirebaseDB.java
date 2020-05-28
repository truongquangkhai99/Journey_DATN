package com.example.journey_datn.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDB {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String userId;
    private List<User> listUser = new ArrayList<>();
    List<Entity> listEntity = new ArrayList<>();
    List<String> listKeyEntity = new ArrayList<>();
    List<Diary> listDiary = new ArrayList<>();
    List<String> listKeyDiary = new ArrayList<>();
    ChildEventListener mChildEventListener;

    public FirebaseDB(String id) {
        this.userId = id;
    }

    public FirebaseDB() {
        loadAllUser();
    }

    public List<Entity> getAllEntity() {
         mDatabase =  FirebaseDatabase.getInstance().getReference().child(userId).child(BranchDBFB.entityBranch);
         ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Entity entity = dataSnapshot.getValue(Entity.class);
                listKeyEntity.add(dataSnapshot.getKey());
                listEntity.add(entity);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Entity entity = dataSnapshot.getValue(Entity.class);
                String entityKey = dataSnapshot.getKey();
                int entityIndex = listKeyEntity.indexOf(entityKey);
                if (entityIndex > -1) {
                    listEntity.set(entityIndex, entity);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String entityKey = dataSnapshot.getKey();
                int entityIndex = listKeyEntity.indexOf(entityKey);
                if (entityIndex > -1) {
                    listKeyEntity.remove(entityIndex);
                    listEntity.remove(entityIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
        return listEntity;
    }

    public void insertEntity(Entity entity) {
        mDatabase.child(userId).child(BranchDBFB.entityBranch).child(entity.getId()).setValue(entity);
    }

    public void updateEntity(String entityId, Entity entity) {
        mDatabase.child(userId).child(BranchDBFB.entityBranch).child(entityId).setValue(entity);
    }

    public void deleteEntity(String entityID) {
        mDatabase.child(userId).child(BranchDBFB.entityBranch).child(entityID).setValue(null);
    }

    public void deleteAllEntity() {
        mDatabase.child(userId).child(BranchDBFB.entityBranch).removeValue();
    }

    /**
     * lấy ra các Entity theo thời gian truyền vào sử dụng trong FragmentCalendar
     * @param entityList
     * @param day
     * @param month
     * @param year
     * @return
     */
    public List<Entity> getEntityByTime(List<Entity> entityList, int day, int month, int year) {
        List<Entity> list = new ArrayList(), allList;
        allList = entityList;
        for (Entity entity : allList)
            if (entity.getDay() == day && entity.getMonth() == month && entity.getYear() == year)
                list.add(entity);
        return list;
    }


    public List<Diary> getAllDiary() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId).child(BranchDBFB.diaryBranch);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Diary diary = dataSnapshot.getValue(Diary.class);
                listKeyDiary.add(dataSnapshot.getKey());
                listDiary.add(diary);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Diary diary = dataSnapshot.getValue(Diary.class);
                String diaryKey = dataSnapshot.getKey();
                int diaryIndex = listKeyDiary.indexOf(diaryKey);
                if (diaryIndex > -1) {
                    listDiary.set(diaryIndex, diary);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String diaryKey = dataSnapshot.getKey();
                int diaryIndex = listKeyDiary.indexOf(diaryKey);
                if (diaryIndex > -1) {
                    listKeyDiary.remove(diaryIndex);
                    listDiary.remove(diaryIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
        return listDiary;
    }

    public void insertDiary(Diary diary) {
        mDatabase.child(userId).child(BranchDBFB.diaryBranch).child(diary.getId()).setValue(diary);
    }

    public void updateDiary(String diaryId, Diary diary) {
        mDatabase.child(userId).child(BranchDBFB.diaryBranch).child(diaryId).setValue(diary);
    }

    public void deleteDiary(String diaryId) {
        mDatabase.child(userId).child(BranchDBFB.diaryBranch).child(diaryId).removeValue();
    }

    public void deleteAllDiary() {
        mDatabase.child(userId).child(BranchDBFB.diaryBranch).removeValue();
    }

    public void insertUser(User user) {
        mDatabase.child(user.getId()).child(BranchDBFB.userBranch).setValue(user);
    }


    private void loadAllUser() {
        List<String> arr = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String key = messageSnapshot.getKey() + "";
                    arr.add(key);
                }
                for (String str : arr) {
                    mDatabase.child(str).child(BranchDBFB.userBranch).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String id = (String) dataSnapshot.child("id").getValue();
                            String firstName = (String) dataSnapshot.child("firstName").getValue();
                            String lastName = (String) dataSnapshot.child("lastName").getValue();
                            String username = (String) dataSnapshot.child("username").getValue();

                            listUser.add(new User(id, firstName, lastName, username));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public List<User> getAllUser() {
        return listUser;
    }

}

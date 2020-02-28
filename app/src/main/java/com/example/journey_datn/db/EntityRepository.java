package com.example.journey_datn.db;

import com.example.journey_datn.Model.Entity;

import java.util.List;

import io.reactivex.Flowable;

public class EntityRepository implements EntityDataSource {

    private static EntityRepository sInstance;

    public static EntityRepository getInstance(EntityDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new EntityRepository(localDataSource);
        }
        return sInstance;
    }

    private EntityDataSource mLocalDataSource;

    public EntityRepository(EntityDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public Entity getByEntityId(int entityID) {
        return mLocalDataSource.getByEntityId(entityID);
    }

    @Override
    public List<Entity> getByContent(String content) {
        return mLocalDataSource.getByContent(content);
    }

    @Override
    public Flowable<List<Entity>> getALlEntity() {
        return mLocalDataSource.getALlEntity();
    }

    @Override
    public void insertEntity(Entity entities) {
        mLocalDataSource.insertEntity(entities);
    }

    @Override
    public void deleteEntity(Entity entity) {
        mLocalDataSource.deleteEntity(entity);
    }

    @Override
    public void deleteAllEntity() {
        mLocalDataSource.deleteAllEntity();
    }

    @Override
    public void updateEntity(Entity entities) {
        mLocalDataSource.updateEntity(entities);
    }
}

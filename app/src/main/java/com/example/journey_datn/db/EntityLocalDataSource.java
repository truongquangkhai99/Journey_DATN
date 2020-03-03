package com.example.journey_datn.db;

import com.example.journey_datn.Model.Entity;

import java.util.List;

import io.reactivex.Flowable;

public class EntityLocalDataSource implements EntityDataSource {

    private static EntityLocalDataSource sInstance;

    public static EntityLocalDataSource getInstance(EntityDAO entityDAO) {
        if (sInstance == null) {
            sInstance = new EntityLocalDataSource(entityDAO);
        }
        return sInstance;
    }

    private EntityDAO mEntityDAO;

    public EntityLocalDataSource(EntityDAO entityDAO) {
        mEntityDAO = entityDAO;
    }

    @Override
    public Entity getByEntityId(int entityID) {
        return mEntityDAO.getEntityById(entityID);
    }

    @Override
    public List<Entity> getByContent(String content) {
        return (List<Entity>) mEntityDAO.getEntityByContent(content);
    }


    @Override
    public Flowable<List<Entity>> getALlEntity() {
        return mEntityDAO.getAllEntity();
    }

    @Override
    public void insertEntity(Entity entities) {
       mEntityDAO.insertEntity(entities);
    }

    @Override
    public void deleteEntity(Entity entity) {
        mEntityDAO.deleteEntity(entity);
    }

    @Override
    public void deleteAllEntity() {
        mEntityDAO.deleteAll();
    }

    @Override
    public void updateEntity(Entity entities) {
        mEntityDAO.updateEntity(entities);
    }

    @Override
    public int getCountItemEntity() {
        return mEntityDAO.getCountItem();
    }
}

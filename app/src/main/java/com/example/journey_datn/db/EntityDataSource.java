package com.example.journey_datn.db;

import com.example.journey_datn.Model.Entity;

import java.util.List;

import io.reactivex.Flowable;

public interface EntityDataSource {

    Entity getByEntityId(int entityID);

    List<Entity> getByContent(String content);

    Flowable<List<Entity>> getALlEntity();

    void insertEntity(Entity entities);

    void deleteEntity(Entity entity);

    void deleteAllEntity();

    void updateEntity(Entity entities);

    int getCountItemEntity();
}

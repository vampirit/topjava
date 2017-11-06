package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MockDBMap;

import java.util.*;

public class MealDaoMap implements MealDao {
    private final MockDBMap dataBase;

    public MealDaoMap() {
        dataBase = new MockDBMap();
    }


    @Override
    public void create(Meal meal) {
        dataBase.create(meal);
    }

    @Override
    public Meal getById(int id) {
        return dataBase.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return dataBase.getAll();
    }

    @Override
    public void update(Meal meal) {
        dataBase.update(meal);
    }

    @Override
    public void delete(int id) {
        dataBase.delete(id);
    }


}

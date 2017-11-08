package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MealDaoMap implements MealDao {
    private static final ConcurrentHashMap<Integer, Meal> dataBase = new ConcurrentHashMap<>();
    private static int lastId = 1;
    private static final Lock lock = new ReentrantLock();

    public MealDaoMap() {
        initData();
    }

    private void initData() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        for (int i = 0; i < meals.size(); i++) {
            meals.get(i).setId(lastId++);
            dataBase.put(meals.get(i).getId(), meals.get(i));
        }
    }

    @Override
    public void create(Meal meal){
        lock.lock();
        int currentId = lastId++;
        lock.unlock();
        meal.setId(currentId);
        dataBase.put(currentId, meal);
    }

    @Override
    public void delete(int id){
        dataBase.remove(id);
    }

    @Override
    public void update(Meal meal){
        dataBase.put(meal.getId(), meal);
    }

    @Override
    public Meal getById(int id){
        return dataBase.get(id);
    }

    @Override
    public List<Meal> getAll(){
        return new ArrayList<>(dataBase.values());
    }


}

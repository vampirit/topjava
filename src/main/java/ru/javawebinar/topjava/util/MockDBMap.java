package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MockDBMap {
    private final ConcurrentHashMap<Integer, Meal> dataBase = new ConcurrentHashMap<>();
    private int lastId = 1;
    private final Lock lock = new ReentrantLock();

    public MockDBMap() {
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
            dataBase.put(lastId++, meals.get(i));
        }
    }

    public void create(Meal meal){
        lock.lock();
        int currentId = lastId++;
        lock.unlock();
        dataBase.put(currentId, meal);
    }

    public void delete(int id){
        dataBase.remove(id);
    }

    public void update(Meal meal){
        dataBase.put(meal.getId(), meal);
    }

    public Meal get(int id){
        return dataBase.get(id);
    }

    public List<Meal> getAll(){
        return new ArrayList<>(dataBase.values());
    }
}

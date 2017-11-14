package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    Meal create(Meal meal);

    boolean delete(int mealId, int userId);

    Meal get(int mealId, int userId);

    void update(int userId, Meal meal);
    
    List<Meal> getAll(int userId);

    List<Meal> getBetweenDate(LocalDate startDay, LocalDate endDay, int userId);
}
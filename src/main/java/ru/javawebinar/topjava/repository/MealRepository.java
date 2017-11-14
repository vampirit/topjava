package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    boolean delete(int userId, int mealId);

    Meal get(int userId, int mealId);

    List<Meal> getAll(int userId);

    List<Meal> getBetweenDate(LocalDate startDay, LocalDate endDay, int userId);
}

package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MealRestController {
    private MealService service;

    public List<MealWithExceed> getAll(int userId, int caloriesPerDay){
        return MealsUtil.getWithExceeded(service.getAll(userId), caloriesPerDay);
    }

    public List<MealWithExceed> getBetweenDateTime(LocalDate startDay, LocalDate endDay, LocalTime startTime, LocalTime endTime, int userId, int caloriesPerDay){
        List<Meal> betweenDateTime = service.getBetweenDate(startDay, endDay, userId);
        return MealsUtil.getFilteredWithExceeded(betweenDateTime, startTime, endTime, caloriesPerDay);
    }

    public Meal get(int mealId, int userId){
        return service.get(mealId, userId);
    }

    public Meal add(Meal meal){
        return service.create(meal);
    }

    public void update(Meal meal){
        service.update(meal.getOwnerId(), meal);
    }

    public boolean delete(int userId, int mealId){
        return service.delete(mealId, userId);
    }







}
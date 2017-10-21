package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 7, 0), "До завтрака", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded =
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2005);

        filteredWithExceeded.forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesForAllDay =
                mealList.stream().collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        final Map<LocalDate, Boolean> exceedForAllDay = caloriesForAllDay.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> p.getValue() > caloriesPerDay));

        return mealList.stream()
                .filter(meal -> (TimeUtil.isBetween(meal.getTime(), startTime, endTime)))
                .map(meal -> new UserMealWithExceed(meal, exceedForAllDay.get(meal.getDate())))
                .collect(Collectors.toList());
    }

}

/*

        final Map<LocalDate, Integer> caloriesForAllDay = new HashMap<>();

        return mealList.stream()
                .peek( meal -> caloriesForAllDay.merge( meal.getDate(), meal.getCalories(), (a,b) -> a+b))
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> new UserMealWithExceed(meal, caloriesForAllDay.get(meal.getDate()) < caloriesPerDay))
                .collect(Collectors.toList());
 */

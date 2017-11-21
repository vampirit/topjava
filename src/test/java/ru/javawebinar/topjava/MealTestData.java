package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MealTestData {
    private static final int USER_ID = UserTestData.USER_ID;
    private static final int ADMIN_ID = UserTestData.ADMIN_ID;

    private static List<Meal> allUserMeals = Arrays.asList(
            new Meal(100002, LocalDateTime.of(2015, 5,15, 9,5), "Вася завтрак", 300),
            new Meal(100003, LocalDateTime.of(2015, 5,15, 14,31, 12), "Вася обед", 700),
            new Meal(100004, LocalDateTime.of(2015, 5,15, 18,12), "Вася ужин", 500),
            new Meal(100005, LocalDateTime.of(2015, 5,15, 22,0), "Вася сериальчик", 1531),
            new Meal(100006, LocalDateTime.of(2015, 5,16, 11,24), "Толстый Вася - диетический завтрак", 152),
            new Meal(100007, LocalDateTime.of(2015, 5,16, 14,33), "Вася немного скинул надо пожрать макдак", 544)
    );

    private static List<Meal> allAdminMeals = Arrays.asList(
            new Meal(100008, LocalDateTime.of(2015, 5,15, 9,0), "Админ спортсмен, завтракет тик-таком", 2),
            new Meal(100009, LocalDateTime.of(2015, 5,15, 13,27), "Админ обед - 5 рисинок и 2 фасолины", 23),
            new Meal(100010, LocalDateTime.of(2015, 5,15, 18,18), "Админ уволился и пошел в макдак, где встретил Васю", 1325),
            new Meal(100011, LocalDateTime.of(2015, 5,15, 20,7), "У админа пустой ходильник", 0),
            new Meal(100012, LocalDateTime.of(2015, 5,15, 22,44), "Поймал голубя", 347),
            new Meal(100013, LocalDateTime.of(2015, 5,16, 12,31), "Не завтракал ушел искать работу", 0),
            new Meal(100014, LocalDateTime.of(2015, 5,16, 15,31), "Собеседование в гугле, там есть столовка =)", 1500),
            new Meal(100015, LocalDateTime.of(2015, 5,16, 18,55), "В гугл не взяли, украл пончики", 592),
            new Meal(100016, LocalDateTime.of(2015, 5,16, 20,17), "Встретил Васю, Вася поделился тик-таком", 2)
    );


    public static List<Meal> getAllData(int userId){
        if (userId == ADMIN_ID)
            return new ArrayList<>(allAdminMeals);
        else if (userId == USER_ID)
            return new ArrayList<>(allUserMeals);
        else return Collections.emptyList();
    }

    public static List<Meal> getBetween(LocalDateTime start, LocalDateTime end, int userId){
        if (userId == USER_ID){
            return allUserMeals.stream()
                    .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), start, end))
                    .collect(Collectors.toList());
        }else if (userId == ADMIN_ID){
            return allAdminMeals.stream()
                    .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), start, end))
                    .collect(Collectors.toList());
        }else return Collections.emptyList();

    }
}

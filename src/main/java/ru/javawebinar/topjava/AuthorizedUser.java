package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class AuthorizedUser {
    private static User user;

    public static int id() {
        return user!=null? user.getId() : -1;
    }

    public static int getCaloriesPerDay() {
        return user!=null? user.getCaloriesPerDay() : -1;
    }

    public static void setUser(User user) {
        AuthorizedUser.user = user;
    }

    public static User getUser() {
        return user;
    }
}
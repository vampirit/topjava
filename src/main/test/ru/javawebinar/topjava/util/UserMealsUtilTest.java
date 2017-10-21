package ru.javawebinar.topjava.util;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.*;

public class UserMealsUtilTest {
    private List<UserMeal> userMeals;


    @Before
    public void initList(){
        userMeals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 7, 0), "До завтрака", 510)
        );
    }

    @Test
    public void getFilteredWithExceeded() throws Exception {
        List<UserMealWithExceed> filteredWithExceeded = UserMealsUtil.getFilteredWithExceeded(userMeals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2005);

        List<UserMealWithExceed> expected = new ArrayList<>();
        expected.add(new UserMealWithExceed(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, false));
        expected.add(new UserMealWithExceed(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, true));
        expected.add(new UserMealWithExceed(LocalDateTime.of(2015, Month.MAY, 31, 7, 0), "До завтрака", 510, true));


        assertThat(filteredWithExceeded.size()).isEqualTo(expected.size());
        assertThat(expected).containsAll(filteredWithExceeded);
    }

}
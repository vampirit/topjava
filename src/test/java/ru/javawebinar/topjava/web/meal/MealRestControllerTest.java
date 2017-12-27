package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.web.json.JsonUtil.writeValue;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';


    @Autowired
    protected MealService mealService;

    @Test
    public void deleteMeal() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        List<Meal> meals = new ArrayList<>(MEALS);
        meals.remove(MEAL1);
        assertMatch(mealService.getAll(UserTestData.USER_ID), meals);
    }

    @Test
    public void getAll() throws Exception {
        List<MealWithExceed> withExceeded = MealsUtil.getWithExceeded(mealService.getAll(AuthorizedUser.id()), AuthorizedUser.getCaloriesPerDay());
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writeValue(withExceeded)));

    }

    @Test
    public void createWithLocation() throws Exception {
        Meal created = MealTestData.getCreated();
        ResultActions resultActions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(writeValue(created)))
                .andDo(print())
                .andExpect(status().isCreated());

        Meal meal = TestUtil.readFromJson(resultActions, Meal.class);

        List<Meal> meals = new ArrayList<>(MEALS);
        meals.add(0, meal);
        assertMatch(mealService.getAll(UserTestData.USER_ID), meals);
    }

    @Test
    public void update() throws Exception {

        Meal updated = MealTestData.getUpdated();
        mockMvc.perform(put(REST_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());

        List<Meal> meals = new ArrayList<>(MEALS);
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId().equals(updated.getId()))
                meals.set(i, updated);
        }

        assertMatch(mealService.getAll(UserTestData.USER_ID), meals);
    }

    @Test
    public void getBetweenDateTime() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 23, 59);

        List<MealWithExceed> filteredWithExceeded = MealsUtil.getFilteredWithExceeded(
                mealService.getBetweenDates(start.toLocalDate(), end.toLocalDate(), AuthorizedUser.id()),
                start.toLocalTime(), end.toLocalTime(), AuthorizedUser.getCaloriesPerDay());

        mockMvc.perform(post(REST_URL + "/between")
                .contentType(MediaType.TEXT_HTML_VALUE)
                .param("startDate", start.toLocalDate().toString())
                .param("startTime", end.toLocalTime().toString())
                .param("endDate", end.toLocalDate().toString())
                .param("endTime", end.toLocalTime().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writeValue(filteredWithExceeded)));


    }

}
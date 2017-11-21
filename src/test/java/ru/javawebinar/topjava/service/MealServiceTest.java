package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@RunWith(SpringRunner.class)
public class MealServiceTest {

    @Autowired
    MealService service;
    Meal saveMeal = new Meal(MealTestData.SAVE_MEAL);
    Meal expectedMealUser = new Meal(MealTestData.EXPECTED_MEAL_USER);

    int userId = MealTestData.USER_ID;
    int adminId = MealTestData.ADMIN_ID;

    @Test
    public void getOwn() throws Exception {
        Meal meal = service.get(expectedMealUser.getId(), userId);
        assertThat(meal).isEqualTo(expectedMealUser);
    }

    @Test(expected = NotFoundException.class)
    public void getAlien() throws Exception {
        Meal meal = service.get(expectedMealUser.getId(), adminId);
        assertThat(meal).isEqualTo(expectedMealUser);
    }

    @Test
    public void deleteOwnMeal() throws Exception {
        service.delete(expectedMealUser.getId(), userId);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlienMeal() throws Exception {
        service.delete(expectedMealUser.getId(), adminId);
    }

    @Test
    public void getBetweenDateTimesWithoutOrdered() throws Exception {
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2015,5,15), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2015,5,16), LocalTime.MAX);
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(start, end, userId);
        List<Meal> between = MealTestData.getBetween(start, end, userId);

        assertFirstConatinsAllSecondInAnyOrder(betweenDateTimes, between);
    }



    @Test
    public void getBetweenDateTimesWithOrdered() throws Exception {
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2015,5,15), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2015,5,16), LocalTime.MAX);
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(start, end, userId);
        assertListOrderedByDateReverse(betweenDateTimes);
    }


    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(userId);
        List<Meal> allData = MealTestData.getAllData(userId);
        assertFirstConatinsAllSecondInAnyOrder(all, allData);
    }

    @Test
    public void updateOwn() throws Exception {
        Meal meal = service.get(expectedMealUser.getId(), userId);
        meal.setDescription("change in test Own meal");
        Meal update = service.update(meal, userId);

        assertThat(update).isEqualTo(meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlien() throws Exception {
        Meal meal = service.get(expectedMealUser.getId(), userId);
        meal.setDescription("change in test Alien meal");
        Meal update = service.update(meal, adminId);

        assertThat(update).isEqualTo(meal);
    }

    @Test
    public void create() throws Exception {
        Meal save = service.create(new Meal(saveMeal), userId);
        assertThat(save.getId()).isNotNull();

        Meal meal = service.get(save.getId(), userId);
        assertThat(meal).isEqualToIgnoringGivenFields(saveMeal, "id");
    }


    private void assertListOrderedByDateReverse(List<Meal> all) {
        for (int i = 1; i < all.size() ; i++) {
            assertThat(all.get(i).getDateTime())
                    .isBeforeOrEqualTo(all.get(i-1).getDateTime());
        }
    }

    private void assertFirstConatinsAllSecondInAnyOrder(List<Meal> first, List<Meal> second) {
        assertThat(first).containsExactlyInAnyOrder(second.toArray(new Meal[second.size()]));
    }
}
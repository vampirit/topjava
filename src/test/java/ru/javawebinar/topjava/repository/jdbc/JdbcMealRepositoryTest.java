package ru.javawebinar.topjava.repository.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@RunWith(SpringRunner.class)
public class JdbcMealRepositoryTest {

    @Autowired
    MealRepository repository;
    static Meal saveMeal;
    static Meal expectedMealUser;
    static Meal expectedMealAdmin;

    int userId = 100000;
    int adminId = 100001;

    @BeforeClass
    public static void initMeals(){
        saveMeal = new Meal(LocalDateTime.of(2014, 11, 25, 14, 22),
                            "Хавчик хавчик текстим хавчик", 10001);
        expectedMealUser = new Meal(100005, LocalDateTime.of(2015, 5, 15, 22, 0),
                            "Вася сериальчик", 1531);
        expectedMealAdmin = new Meal(100011, LocalDateTime.of(2015, 5, 15, 22, 7),
                            "У админа пустой ходильник", 0);

    }

    @Test
    public void save() throws Exception {
        Meal save = repository.save(new Meal(saveMeal), userId);
        assertThat(save).isEqualToIgnoringGivenFields(saveMeal,"id");
        assertThat(save.getId()).isNotNull();

        Meal meal = repository.get(save.getId(), userId);
        assertThat(meal).isEqualTo(save);
    }

    @Test
    public void deleteOwnMeal() throws Exception {
        boolean delete = repository.delete(expectedMealUser.getId(), userId);
        assertThat(delete).isTrue();
    }

    @Test
    public void deleteAlienMeal() throws Exception {
        boolean delete = repository.delete(expectedMealAdmin.getId(), userId);
        assertThat(delete).isFalse();
    }



    @Test
    public void getOwnMeal() throws Exception {
        Meal meal = repository.get(expectedMealUser.getId(), userId);
        assertThat(meal).isEqualTo(expectedMealUser);
    }


    @Test
    public void getAlienMeal() throws Exception {
        Meal meal = repository.get(expectedMealUser.getId(), adminId);
        assertThat(meal).isNull();
    }

    @Test
    public void getAllUserData() throws Exception {
        getAllMatches(userId, MealTestData.getAllData(userId));
    }

    @Test
    public void getAllAdminData() throws Exception {
        getAllMatches(adminId, MealTestData.getAllData(adminId));
    }

    @Test
    public void getAllNoNameData() throws Exception {
        getAllMatches(1, MealTestData.getAllData(1));
    }

    private void getAllMatches(int userId, List<Meal> allData) {
        List<Meal> all = repository.getAll(userId);
        assertThat(all).containsAll(allData);
        assertThat(allData).containsAll(all);
    }

    @Test
    public void getAllOrdered() throws Exception{
        List<Meal> all = repository.getAll(userId);
        isOrderedByDateReverse(all);
    }



    @Test
    public void getBetweenOrdered(){
        LocalDate date = LocalDate.of(2015,5,15);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

        List<Meal> between = repository.getBetween(start, end, userId);

        isOrderedByDateReverse(between);
    }

    private void isOrderedByDateReverse(List<Meal> all) {
        for (int i = 1; i < all.size() ; i++) {
            assertThat(all.get(i).getDateTime())
                    .isBeforeOrEqualTo(all.get(i-1).getDateTime());
        }
    }

    @Test
    public void getMealBetweenOneDay() throws Exception {
        LocalDate date = LocalDate.of(2015,5,15);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

        assertDataBetweenDatesEquals(start, end, userId);
    }

    @Test
    public void getMealBetweenTwoDays() throws Exception {
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2015,5,15), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2015,5,16), LocalTime.MAX);

        assertDataBetweenDatesEquals(start, end, adminId);
    }

    @Test
    public void getMealBetweenOneDayWithNoData() throws Exception {
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2015,5,5), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2015,5,5), LocalTime.MAX);

        assertDataBetweenDatesEquals(start, end, adminId);
    }

    private void assertDataBetweenDatesEquals(LocalDateTime start, LocalDateTime end, int id) {
        List<Meal> between = repository.getBetween(start, end, id);
        List<Meal> allData = MealTestData.getBetween(start, end, id);
        assertThat(between).containsAll(allData);
        assertThat(allData).containsAll(between);
    }

}
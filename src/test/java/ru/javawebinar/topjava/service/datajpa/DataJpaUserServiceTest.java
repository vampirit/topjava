package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    public void testGetWithMeals() throws Exception {
        User admin = service.getWithMeals(ADMIN_ID);
        assertMatch(admin, ADMIN);
        MealTestData.assertMatch(admin.getMeals(), MealTestData.ADMIN_MEAL2, MealTestData.ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithMealsNotFound() throws Exception {
        service.getWithMeals(1);
    }

    @Test
    public void testEnabledFalse() throws Exception {
        service.enabledUser(ADMIN.getId(), false);
        User user = service.get(ADMIN.getId());
        assertThat(user).hasFieldOrPropertyWithValue("enabled", false);
    }

    @Test
    public void testEnabledTrue() throws Exception {
        service.enabledUser(ADMIN.getId(), true);
        User user = service.get(ADMIN.getId());
        assertThat(user).hasFieldOrPropertyWithValue("enabled", true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEnabledNotFound() throws Exception {
        service.enabledUser(777, true);
//        User user = service.get(777);
//        assertThat(user).hasFieldOrPropertyWithValue("enabled", true);
    }


}
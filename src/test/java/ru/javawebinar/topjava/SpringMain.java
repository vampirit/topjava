package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try(ConfigurableApplicationContext parentCtx = new ClassPathXmlApplicationContext()) {
            ConfigurableEnvironment environment = parentCtx.getEnvironment();
            environment.addActiveProfile(Profiles.POSTGRES_DB);
            environment.addActiveProfile(Profiles.DATAJPA);
            parentCtx.refresh();

            try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, parentCtx)) {
                System.out.println("Bean definition names: ");
                Arrays.stream(appCtx.getBeanDefinitionNames()).forEachOrdered(System.out::println);
                AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
                adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
                System.out.println();

                MealRestController mealController = appCtx.getBean(MealRestController.class);
                List<MealWithExceed> filteredMealsWithExceeded =
                        mealController.getBetween(
                                LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                                LocalDate.of(2015, Month.MAY, 31), LocalTime.of(13, 0));
                filteredMealsWithExceeded.forEach(System.out::println);
            }
        }
    }
}

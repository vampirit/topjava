package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));

            MealRestController bean = appCtx.getBean(MealRestController.class);
            System.out.println(bean.delete(-1, 8));
//            bean.update(new Meal(7, LocalDateTime.now(), "test", 322, -1));
            List<MealWithExceed> all = bean.getAll(-1, 10000);
            System.out.println("\n all meals for user id = -1");
            all.forEach(System.out::println);

            List<MealWithExceed> all2 = bean.getAll(2, 10000);
            System.out.println("\n all meals for user id = 2");
            all2.forEach(System.out::println);
        }
    }
}

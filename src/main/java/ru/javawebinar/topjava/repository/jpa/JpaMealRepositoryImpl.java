package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = manager.getReference(User.class, userId);

        if (meal.isNew()){
            meal.setUser(user);
            manager.persist(meal);
            return meal;
        }else {
            Meal mealFromDb = get(meal.getId(), userId);
            if (mealFromDb == null) {
                return null;
            }
        }
        meal.setUser(user);

        return manager.merge(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        int countDelete = manager.createNamedQuery(Meal.DELETE)
                .setParameter("userId", userId)
                .setParameter("mealId", id)
                .executeUpdate();

        return countDelete != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = manager.find(Meal.class, id);

        if (meal == null || !meal.getUser().getId().equals(userId))
            return null;

        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> allMeal = manager.createNamedQuery(Meal.GET_ALL, Meal.class).setParameter("userId", userId).getResultList();
        for (Meal o : allMeal) {
            System.out.println(o.getUser());
        }
        return allMeal;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return manager.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDay", startDate)
                .setParameter("endDay", endDate)
                .getResultList();
    }
}
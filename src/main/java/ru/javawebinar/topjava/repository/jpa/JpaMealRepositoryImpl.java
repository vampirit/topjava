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
@Transactional
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Meal save(Meal meal, int userId) {
        User user = manager.find(User.class, userId);

        if (meal.isNew()){
            meal.setUser(user);
            manager.persist(meal);
            return meal;
        }else{
            Meal mealFromDb = get(meal.getId(), userId);
            if (mealFromDb == null) {
                return null;
            }else
                meal.setUser(mealFromDb.getUser());
        }

        return manager.merge(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        int countDelete = manager.createNamedQuery(Meal.DELETE)
                .setParameter("userId", userId)
                .setParameter("mealId", id)
                .executeUpdate();

        return countDelete != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = manager.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("userId", userId)
                .setParameter("mealId", id)
                .getResultList();

        if (meals.size() == 0)
            return null;

        return meals.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> userId1 = manager.createNamedQuery(Meal.GET_ALL, Meal.class).setParameter("userId", userId).getResultList();
        for (Meal o : userId1) {
            System.out.println(o.getUser());
        }
        return userId1;
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
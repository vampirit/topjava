package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        return repository.delete(userId, mealId);
    }

    @Override
    public Meal get(int mealId, int userId) {
        return repository.get(userId, mealId);
    }

    @Override
    public void update(int userId, Meal meal) {
        Meal mealInRepo = repository.get(userId, meal.getId());
        if (mealInRepo == null || !mealInRepo.getOwnerId().equals(userId))
            throw new NotFoundException("user " + userId + " not have meal id="+meal.getId());

        repository.save(meal);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenDate(LocalDate startDay, LocalDate endDay, int userId) {
        return repository.getBetweenDate(startDay, endDay, userId);
    }
}
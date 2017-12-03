package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findAllByUser_IdOrderByDateTimeDesc(int userId);

    Optional<Meal> findByIdAndUser_Id(int id, int userId);

    @Transactional
    int deleteByIdAndUser_Id(int id, int userId);

    List<Meal> findAllByUser_IdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime start, LocalDateTime end);
}

package ru.javawebinar.topjava.web.meal;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

@RestController
@RequestMapping(MealAjaxContoller.AJAX_URL)
public class MealAjaxContoller extends AbstractMealController {

    public static final String AJAX_URL = "/ajax/meals";

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        super.delete(id);
    }

    @PostMapping
    public void save(
            @RequestParam Integer id,
            @RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime dateTime,
            @RequestParam String description,
            @RequestParam Integer calories){
        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew())
            super.create(meal);
    }

    @PostMapping(value = "/filter")
    public List<MealWithExceed> filtered(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = TIME) LocalTime endTime
    ){

        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}

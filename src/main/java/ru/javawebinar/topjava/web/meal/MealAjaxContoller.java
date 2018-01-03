package ru.javawebinar.topjava.web.meal;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(MealAjaxContoller.AJAX_URL)
public class MealAjaxContoller extends AbstractMealController {

    public static final String AJAX_URL = "/ajax/meals/";

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        super.delete(id);
    }



    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @PostMapping
    public void save(
            @RequestParam Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam String description,
            @RequestParam Integer calories){
        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew())
            super.create(meal);
    }
}

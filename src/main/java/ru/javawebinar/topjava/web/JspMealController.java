package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService mealService;

    @GetMapping
    public String getAllMeals(Model model){
        int userId = AuthorizedUser.id();
        int caloriesPerDay = AuthorizedUser.getCaloriesPerDay();
        log.debug("GetAllMeals get request, userId={}", userId);

        List<Meal> all = mealService.getAll(userId);
        List<MealWithExceed> withExceeded = MealsUtil.getWithExceeded(all, caloriesPerDay);
        model.addAttribute("meals", withExceeded);
        return "meals";
    }

    @GetMapping("/update/{id}")
    public String updateMeal(@PathVariable("id") int id, Model model){
        int userId = AuthorizedUser.id();
        log.debug("UpdateMeal get request, userId={}, mealId={}", userId, id);
        Meal meal = mealService.get(id, userId);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "Edit meal");
        return "mealForm";
    }

    @GetMapping("/create")
    public String createMeal(Model model){
        log.debug("CreateMeal get request.");
        model.addAttribute("meal", new Meal());
        model.addAttribute("action", "Create meal");
        return "mealForm";
    }

    @PostMapping("/save")
    public String saveMeal(HttpServletRequest request){
        log.debug("SaveMeal post request.");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (!request.getParameter("id").isEmpty()) {
            meal.setId(Integer.parseInt(request.getParameter("id")));
        }
        log.debug("Save Meal={}", meal);
        int userId = AuthorizedUser.id();

        if (meal.isNew()) {
            mealService.create(meal, userId);
        } else {
            mealService.update(meal, userId);
        }

        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable("id") int id){
        int userId = AuthorizedUser.id();
        log.debug("Delete mealId={}, userId={}", id, userId);
        mealService.delete(id, userId);
        return "redirect:/meals";
    }


}

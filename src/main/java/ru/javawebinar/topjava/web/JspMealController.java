package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    @Autowired
    private MealService mealService;

    @GetMapping
    public String getAllMeals(Model model){
        int id = AuthorizedUser.id();
        List<Meal> all = mealService.getAll(id);
        List<MealWithExceed> withExceeded = MealsUtil.getWithExceeded(all, AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", withExceeded);
        return "meals";
    }




}

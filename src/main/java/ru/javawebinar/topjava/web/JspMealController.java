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
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends MealRestController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAllMeals(Model model){
        List<MealWithExceed> withExceeded = getAll();
        model.addAttribute("meals", withExceeded);
        return "meals";
    }

    @GetMapping("/update/{id}")
    public String updateMeal(@PathVariable("id") int id, Model model){
        Meal meal = get(id);
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
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (!request.getParameter("id").isEmpty()){
            int idMeal = Integer.parseInt(request.getParameter("id"));
            meal.setId(idMeal);
            update(meal, idMeal);
        }else {
            create(meal);
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable("id") int id){
        delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filterMeals(HttpServletRequest request, Model model){
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        List<MealWithExceed> filteredMeals = getBetween(
                startDate.isEmpty() ? null : LocalDate.parse(startDate),
                startTime.isEmpty() ? null : LocalTime.parse(startTime),
                endDate.isEmpty() ? null : LocalDate.parse(endDate),
                endTime.isEmpty() ? null : LocalTime.parse(endTime)
        );

        model.addAttribute("meals", filteredMeals);
        return "meals";
    }
}

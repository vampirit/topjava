package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMap;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private static final MealDao mealDao = new MealDaoMap();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("mealServlet GET");
        if (request.getParameter("action") != null){
            String action = request.getParameter("action");
            if (action.equals("addMeal")){
                LOG.debug("redirect to addEditMeal.jsp");
                request.getRequestDispatcher("/addEditMeal.jsp").forward(request, response);
            }

            else if (action.equals("delete")){
                LOG.debug("request delete " + request.getParameter("id"));
            }else if (action.equals("edit")){
                LOG.debug("request edit " + request.getParameter("id"));
            }
        }

        List<Meal> allMeal = mealDao.getAll();
        List<MealWithExceed> mealExceeded = MealsUtil.getMealExceeded(allMeal, 2000);


        request.setAttribute("meals", mealExceeded);
        request.getRequestDispatcher("/meal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("mealServlet POST");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), formatter);
        String id = req.getParameter("id");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));


        Meal meal = new Meal(dateTime, description, calories);
        if (id != null && !id.isEmpty()){
            meal.setId(Integer.parseInt(id));
        }

        LOG.debug(meal.toString());
        resp.sendRedirect("meals");
    }
}

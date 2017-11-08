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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("mealServlet GET");
        String action = req.getParameter("action");
        if (action!= null) {
            workWithAction(req, resp, action);
        }else {
            List<Meal> allMeal = mealDao.getAll();
            List<MealWithExceed> mealExceeded = MealsUtil.getMealExceeded(allMeal, 2000);

            req.setAttribute("meals", mealExceeded);
            req.getRequestDispatcher("/meal.jsp").forward(req, resp);
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("mealServlet POST");
        req.setCharacterEncoding("UTF-8");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), formatter);
        String id = req.getParameter("id");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));


        Meal meal = new Meal(dateTime, description, calories);
        if (id != null && !id.isEmpty()){
            meal.setId(Integer.parseInt(id));
            mealDao.update(meal);
        }else {
            mealDao.create(meal);
        }

        LOG.debug(meal.toString());
        resp.sendRedirect("meals");
    }

    private void workWithAction(HttpServletRequest req, HttpServletResponse resp, String action) throws ServletException, IOException {
        if (action.equals("addMeal")) {
            addMeal(req, resp);
        } else if (action.equals("edit")) {
            editMeal(req, resp);
        }else if(action.equals("delete")){
            deleteMeal(req, resp);
        }
    }

    private void deleteMeal(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        LOG.debug("request delete " + id);
        mealDao.delete(id);
        resp.sendRedirect("meals");
    }

    private void editMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        LOG.debug("request edit " + id);
        req.setAttribute("meal", mealDao.getById(id));
        req.getRequestDispatcher("/addEditMeal.jsp").forward(req, resp);
    }

    private void addMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirect to addEditMeal.jsp");
        req.getRequestDispatcher("/addEditMeal.jsp").forward(req, resp);
    }

}

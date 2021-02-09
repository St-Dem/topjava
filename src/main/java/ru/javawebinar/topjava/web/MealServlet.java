package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MealServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(MealServlet.class.getName());
    private Storage storage;

    public void init(ServletConfig servletConfig) {
        storage = MealsUtil.storageForMeals;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            if (id == null || id.trim().isEmpty()) {
                storage.create(MealsUtil.createMeal(localDateTime, description, calories));
            } else {
                storage.update(new Meal(id, localDateTime, description, calories));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Wrong input");
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.createMealsTo(storage.getAll()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        MealTo mealTo;

        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                mealTo = MealsUtil.EMPTY_MEALTO;
                break;
            case "update":
                Meal meal = storage.get(id);
                mealTo = MealsUtil.createMealTo(storage.getAll(), meal);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", mealTo);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}

package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


public class MealServlet extends HttpServlet {
    private Storage storage;

    public void init(ServletConfig servletConfig) {
        storage = MealsUtil.storageInMemory;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"), TimeUtil.DATE_TIME_FORMATTER);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id == null || id.trim().length() == 0) {
            storage.save(new Meal(localDateTime, description, calories));
        } else {
            storage.delete(id);
            storage.save(new Meal(localDateTime, description, calories));
        }
        request.setAttribute("meals", MealsUtil.createMealsTo(storage.getAllSorted()));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.createMealsTo(storage.getAllSorted()));
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
                mealTo = MealTo.EMPTY;
                break;
            case "update":
                Meal meal = storage.get(id);
                mealTo = MealsUtil.createMealTo(storage.getAllSorted(), meal);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", mealTo);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}

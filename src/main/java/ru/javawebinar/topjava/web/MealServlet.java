package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.storage.StorageMeals;
import ru.javawebinar.topjava.util.MealsUtil;
import org.slf4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage storage;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        storage = new StorageMeals();
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        storage.create(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("doPost ready");
        String id = request.getParameter("id");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id == null || id.trim().isEmpty()) {
            storage.create(localDateTime, description, calories);
        } else {
            storage.update(new Meal(id, localDateTime, description, calories));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet ready");
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.createMealsTo(storage.getAll()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal = StorageMeals.EMPTY_MEAL;

        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                break;
            case "update":
                meal = storage.get(id);
                break;
            default:
                request.setAttribute("meals", MealsUtil.createMealsTo(storage.getAll()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}

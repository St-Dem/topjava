package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> allMeals = new ConcurrentHashMap<>();
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);



    {
        allMeals.put(1, repository);
        MealsUtil.meals.forEach(x -> save(1, x));
        allMeals.put(1, repository);
        repository = new ConcurrentHashMap<>();
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> integerMealMap = getMealMap(userId);
            integerMealMap.put(meal.getId(), meal);
            allMeals.put(userId, integerMealMap);
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> integerMealMap = getMealMap(userId);
        integerMealMap.put(meal.getId(), meal);
        allMeals.put(userId, integerMealMap);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> integerMealMap = getMealMap(userId);
        boolean bool = integerMealMap.remove(id) != null;
        allMeals.put(userId, integerMealMap);
        return bool;
    }

    @Override
    public Meal get(int userId, int id) {
        try {
            return getMealMap(userId).get(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
      return getMealMap(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMealMap(int userId) {
        return allMeals.get(userId);
    }
}



package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
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
        for (Meal meal : MealsUtil.meals) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        }
        allMeals.put(SecurityUtil.authUserId(), repository);
        repository = new ConcurrentHashMap<>();
        for(int i = 0; i < MealsUtil.meals.size(); i = i+2){
            repository.put(MealsUtil.meals.get(i).getId(), MealsUtil.meals.get(i));
        }
        allMeals.put(10, repository);
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
                .sorted((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()) * -1)
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMealMap(int userId) {
        return allMeals.get(userId);
    }
}



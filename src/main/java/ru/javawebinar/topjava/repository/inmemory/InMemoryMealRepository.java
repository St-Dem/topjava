package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Integer userId;
    private final Map<Integer, Map<Integer, Meal>> allMeals = new ConcurrentHashMap<>();
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository(Integer userId) {
        this.userId = userId;
        for (Meal meal : MealsUtil.meals) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        }
        allMeals.put(userId, repository);
    }

    public InMemoryMealRepository() {
        for (Meal meal : MealsUtil.meals) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        }
        allMeals.put(SecurityUtil.authUserId(), repository);
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
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
    public boolean delete(Integer userId, int id) {
        Map<Integer, Meal> integerMealMap = getMealMap(userId);
        boolean bool = integerMealMap.remove(id) != null;
        allMeals.put(userId, integerMealMap);
        return bool;
    }

    @Override
    public Meal get(Integer userId, int id) {
        try {
            return getMealMap(userId).get(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        List<Meal> mealList = new ArrayList<>(getMealMap(userId).values());
        mealList.sort(new DateComparator());
        return mealList;
    }

    private Map<Integer, Meal> getMealMap(Integer userId) {
        return allMeals.get(userId);
    }

    static class DateComparator implements Comparator<Meal> {
        @Override
        public int compare(Meal o1, Meal o2) {
            return -(o1.getDate().compareTo(o2.getDate()));
        }
    }
}



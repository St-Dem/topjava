package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        Map<LocalDate, UserMeal> mealMap = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            LocalDate mealDate = mealDateTime.toLocalDate();
            caloriesByDays.put(mealDate, (caloriesByDays.getOrDefault(mealDate, 0) + meal.getCalories()));
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                mealMap.put(mealDate, meal);
            }
        }
        Map<LocalDate, UserMealWithExcess> mealsWithExcess = new HashMap<>();
        mealMap.forEach((k, v) -> mealsWithExcess.put(k, new UserMealWithExcess(v, caloriesByDays.get(k) > caloriesPerDay)));
        return new ArrayList<>(mealsWithExcess.values());
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = meals.stream().collect(Collectors.toMap(
                meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum
        ));

        return meals.stream()
                .filter(mealTime -> TimeUtil.isBetweenHalfOpen(mealTime.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    int excess = caloriesByDays.get(userMeal.getDateTime().toLocalDate());
                    return new UserMealWithExcess(userMeal, excess > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}

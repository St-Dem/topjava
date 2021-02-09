package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.StorageForMeals;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class MealsUtil {
    public static List<Meal> meals;
    public final static int CALORIES_PER_DAY = 2000;
    public static StorageForMeals storageForMeals = new StorageForMeals();
    public final static Meal EMPTY_MEAL;
    public final static MealTo EMPTY_MEALTO;

    static {
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storageForMeals.create(createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        EMPTY_MEAL = new Meal(null, LocalDateTime.now(), "", 0);
        EMPTY_MEALTO = new MealTo(null, LocalDateTime.now(), "", 0, false);

    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        meals = Arrays.asList(
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                createMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );


        System.out.println("----");
        final LocalTime startTime = LocalTime.of(7, 0);
        final LocalTime endTime = LocalTime.of(12, 0);

        List<MealTo> mealsTo = filteredByStreams(meals, startTime, endTime, CALORIES_PER_DAY);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByCycles(meals, startTime, endTime, CALORIES_PER_DAY));

    }

    public static List<MealTo> createMealsTo(List<Meal> meals) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .map(meal -> createMealToWithId(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }

    public static MealTo createMealTo(List<Meal> meals, Meal meal) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return createMealToWithId(meal, (caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY));
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createMealTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealTo> mealsTo = new ArrayList<>();
        meals.forEach(meal -> {
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                mealsTo.add(createMealTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsTo;
    }

    private static MealTo createMealTo(Meal meal, boolean excess) {
        return new MealTo(UUID.randomUUID().toString(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static MealTo createMealToWithId(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static Meal createMeal (LocalDateTime dateTime, String description, int calories){
        return new Meal(UUID.randomUUID().toString(), dateTime, description, calories);
    }

    public static MealTo createMealTo(LocalDateTime dateTime, String description, int calories, boolean excess){
        return new MealTo(UUID.randomUUID().toString(), dateTime, description, calories, excess);
    }
}

package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StorageMeals implements Storage {
    private Map<String, Meal> map = new ConcurrentHashMap<>();
    public static StorageMeals storageMeals = new StorageMeals();
    public final static Meal EMPTY_MEAL;
    public final static MealTo EMPTY_MEALTO;

    static {
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        storageMeals.create(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

        EMPTY_MEAL = new Meal(null, LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)), "", 0);
        EMPTY_MEALTO = new MealTo(null, LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)), "", 0, false);
    }

    public void update(Meal meal) {
        String id = meal.getId();
        map.put(id, meal);
    }

    public Meal create(LocalDateTime dateTime, String description, int calories) {
        String id = UUID.randomUUID().toString();
        return map.putIfAbsent(id, new Meal(id, dateTime, description, calories));
    }

    public void delete(String id) {
        map.remove(id);
    }

    public Meal get(String id) {
        return map.get(id);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    public static Meal createMeal(LocalDateTime dateTime, String description, int calories) {
        return new Meal(UUID.randomUUID().toString(), dateTime, description, calories);
    }

    public static MealTo createMealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        return new MealTo(UUID.randomUUID().toString(), dateTime, description, calories, excess);
    }
}

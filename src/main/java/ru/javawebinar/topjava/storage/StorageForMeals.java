package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageForMeals implements Storage{
    private Map<String, Meal> map = new ConcurrentHashMap<>();

    public void update(Meal meal) {
        String id = meal.getId();
        map.put(id, meal);
    }

    public void create(Meal meal) {
        String id = meal.getId();
        map.putIfAbsent(id, meal);
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
}

package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageMeals implements Storage {
    private Map<String, Meal> map = new ConcurrentHashMap<>();

    public Meal update(Meal meal) {
        return map.put(meal.getId(), meal);
    }

    public Meal create(Meal meal) {
        return map.putIfAbsent(meal.getId(), meal);
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

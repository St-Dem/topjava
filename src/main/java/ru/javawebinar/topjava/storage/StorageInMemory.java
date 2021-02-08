package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageInMemory extends AbstractStorage<String>{
    private Map<String, Meal> map = new HashMap<>();

    @Override
    protected String getSearchKey(String id) {
        return id;
    }

    @Override
    protected void doUpdate(Meal meal, String id) {
        map.put(id, meal);
    }

    @Override
    protected boolean isExist(String id) {
        return map.containsKey(id);
    }

    @Override
    protected void doSave(Meal meal, String id) {
        map.put(id, meal);
    }

    @Override
    protected Meal doGet(String id) {
        return map.get(id);
    }

    @Override
    protected void doDelete(String id) {
        map.remove(id);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Meal> doCopyAll() {
        return new ArrayList<>(map.values());
    }

}

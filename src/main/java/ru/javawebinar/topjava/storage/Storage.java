package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public interface Storage {
    void update(Meal meal);

    void create(Meal meal);

    Meal get(String id);

    void delete(String id);

    List<Meal> getAll();
}

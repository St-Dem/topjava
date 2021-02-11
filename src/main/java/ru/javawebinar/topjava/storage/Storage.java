package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface Storage {
    Meal update(Meal meal);

    Meal create(Meal meal);

    Meal get(String id);

    void delete(String id);

    List<Meal> getAll();
}

package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface Storage {
    void update(Meal meal);

    Meal create(LocalDateTime dateTime, String description, int calories);

    Meal get(String id);

    void delete(String id);

    List<Meal> getAll();
}

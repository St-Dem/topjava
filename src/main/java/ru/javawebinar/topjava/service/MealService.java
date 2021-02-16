package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(SecurityUtil.authUserId(), meal);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(SecurityUtil.authUserId(), id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(SecurityUtil.authUserId(), id), id);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(repository.getAll(SecurityUtil.authUserId()));
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(SecurityUtil.authUserId(), meal), meal.getId());
    }
}
package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        MealRepository mealRepository = new InMemoryMealRepository();
        mealRepository.save(1, new Meal(LocalDateTime.now(), "", 111));
        mealRepository.getAll(1).forEach(System.out::println);
        System.out.println("-----");
        mealRepository.getAll(10).forEach(System.out::println);
    }
}

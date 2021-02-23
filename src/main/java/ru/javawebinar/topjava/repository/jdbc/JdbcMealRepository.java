package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("user_id", userId)
                .addValue("datetime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());

        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET datetime=:datetime, description=:description, calories=:calories" +
                        " WHERE id=:id", map) == 0) {return null;}
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update(" DELETE FROM meals WHERE  id=?", id) != 0;

    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(" SELECT id, datetime, calories, description FROM meals WHERE user_id=?", ROW_MAPPER, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterList(meal -> true, userId);

    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return filterList(meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDateTime, endDateTime), userId);
    }

    public List<Meal> filterList(Predicate<Meal> filter, int userId) {
        List<Meal> meals = jdbcTemplate.query(" SELECT id, datetime, calories, description FROM meals WHERE user_id=?", ROW_MAPPER, userId);
        return meals.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}


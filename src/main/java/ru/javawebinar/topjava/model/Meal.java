package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.TimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Meal implements Comparable<Meal>{
    private String id;
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;
    public final static Meal EMPTY;

    public void setId(String id) {
        this.id = id;
    }

    static {
        EMPTY = new Meal(TimeUtil.EMPTY, "", 0);
        EMPTY.setId(null);
    }
    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(UUID.randomUUID().toString(), dateTime, description, calories);
    }

    public Meal(String id, LocalDateTime dateTime, String description, int calories){
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public int compareTo(Meal o) {
        return dateTime.compareTo(o.dateTime) != 0 ? dateTime.compareTo(o.dateTime) :
                description.compareTo(o.description) != 0 ? description.compareTo(o.description) :
                        calories > o.calories ? 1 : -1;
    }
}

package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.TimeUtil;
import java.time.LocalDateTime;
import java.util.UUID;

public class MealTo implements Comparable<MealTo> {
    private String id;
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;
    public final static MealTo EMPTY;

    static {
        EMPTY = new MealTo(TimeUtil.EMPTY, "", 0, false);
        EMPTY.setId(null);
    }

    //    private final AtomicBoolean excess;      // or Boolean[1],  filteredByAtomic
//    private final Boolean excess;            // filteredByReflection
//    private final Supplier<Boolean> excess;  // filteredByClosure
    private boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this(UUID.randomUUID().toString(), dateTime, description, calories, excess);
    }

    public MealTo(String id, LocalDateTime dateTime, String description, int calories, boolean excess){
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
//    public Boolean getExcess() {
//        return excess.get();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MealTo mealTo = (MealTo) o;

        if (calories != mealTo.calories) return false;
        if (excess != mealTo.excess) return false;
        if (!id.equals(mealTo.id)) return false;
        if (!dateTime.equals(mealTo.dateTime)) return false;
        return description.equals(mealTo.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + dateTime.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + calories;
        result = 31 * result + (excess ? 1 : 0);
        return result;
    }

    // for filteredBySetterRecursion
    public void setExcess(boolean excess) {
        this.excess = excess;
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

    public boolean isExcess() {
        return excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public int compareTo(MealTo o) {
        return dateTime.compareTo(o.dateTime) != 0 ? dateTime.compareTo(o.dateTime) :
                description.compareTo(o.description) != 0 ? description.compareTo(o.description) :
                        calories > o.calories ? 1 : -1;
    }
}

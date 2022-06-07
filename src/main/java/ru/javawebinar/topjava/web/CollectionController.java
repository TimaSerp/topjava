package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionController implements MealController {
    private static final int CALORIES_PER_DAY = 2000;

    private final AtomicInteger countId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    public CollectionController() {
        List<Meal> meals = MealsUtil.initializeMeals();
        for (Meal meal : meals) {
            addOrUpdateMeal(meal);
        }
    }

    @Override
    public void addOrUpdateMeal(Meal meal) {
        int id = meal.getId();
        if (id >= 0 && !mealMap.isEmpty() && mealMap.containsKey(id)) {
            mealMap.put(id, meal);
        } else {
            meal.setId(countId.get());
            mealMap.put(countId.getAndIncrement(), meal);
        }
    }

    @Override
    public void deleteMeal(int id) {
        mealMap.remove(id);
    }

    @Override
    public Meal getMeal(int id) {
        if (mealMap.containsKey(id)) {
            return mealMap.get(id);
        } else {
            return new Meal(null, null, 0, id);
        }
    }

    @Override
    public List<MealTo> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        for (Map.Entry<Integer, Meal> entry : mealMap.entrySet()) {
            meals.add(entry.getValue());
        }
        return MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY);
    }
}

package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionMealDao implements MealDao {
    private final AtomicInteger countId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    public CollectionMealDao() {
        List<Meal> meals = MealsUtil.getDemoMeals();
        for (Meal meal : meals) {
            save(meal);
        }
    }

    @Override
    public Meal save(Meal meal) {
        Integer id = meal.getId();
        if (id != null && mealMap.containsKey(id)) {
            mealMap.replace(id, meal);
        } else {
            id = countId.getAndIncrement();
            meal.setId(id);
            mealMap.put(id, meal);
        }
        return meal;
    }

    @Override
    public void delete(Integer id) {
        mealMap.remove(id);
    }

    @Override
    public Meal get(Integer id) {
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }
}

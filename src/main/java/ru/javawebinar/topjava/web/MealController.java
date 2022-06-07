package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealController {
    void addOrUpdateMeal(Meal meal);

    void deleteMeal(int id);

    Meal getMeal(int id);

    List<MealTo> getAllMeals();
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("\nFilter by cycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println("\nFilter with recursion");
        List<UserMealWithExcess> mealsTo2 = filteredWithRecursion(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        System.out.println("\nFilter with streams");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesMapByDate = new HashMap<>();
        meals.forEach(meal ->
                caloriesMapByDate.merge(getLocalDate(meal), meal.getCalories(), Integer::sum)
        );
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(getLocalTime(meal), startTime, endTime)) {
                mealsWithExcess.add(getUserMealWithExcess(meal, caloriesMapByDate.get(getLocalDate(meal)) > caloriesPerDay));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredWithRecursion(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        UserMeal meal = meals.get(0);
        caloriesMapByDate.merge(getLocalDate(meal), meal.getCalories(), Integer::sum);
        meals.remove(meal);
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        if (!meals.isEmpty()) {
            mealsWithExcess = filteredWithRecursion(meals, startTime, endTime, caloriesPerDay);
        }
        if (TimeUtil.isBetweenHalfOpen(getLocalTime(meal), startTime, endTime)) {
            mealsWithExcess.add(getUserMealWithExcess(meal, caloriesMapByDate.get(getLocalDate(meal)) > caloriesPerDay));
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesMapByDate = meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getLocalDate,
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(getLocalTime(meal), startTime, endTime))
                .map(meal -> getUserMealWithExcess(meal, caloriesMapByDate.get(getLocalDate(meal)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess getUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static LocalDate getLocalDate(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }

    private static LocalTime getLocalTime(UserMeal meal) {
        return meal.getDateTime().toLocalTime();
    }
}

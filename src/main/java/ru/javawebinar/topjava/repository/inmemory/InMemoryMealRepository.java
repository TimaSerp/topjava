package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.MAX;
import static java.time.LocalDateTime.MIN;
import static ru.javawebinar.topjava.util.MealsUtil.USER_ONE_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, ConcurrentHashMap<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    Comparator<Meal> mealComparator = Comparator.comparing(Meal::getDateTime);


    {
        MealsUtil.meals.forEach(meal -> save(USER_ONE_ID, meal));
//        MealsUtil.meals2.forEach(meal -> save(USER_TWO_ID, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            repository.putIfAbsent(userId, new ConcurrentHashMap<>());
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        ConcurrentHashMap<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        ConcurrentHashMap<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        ConcurrentHashMap<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? Collections.emptyList() : userMeals.values().stream()
                .sorted(mealComparator)
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), toStartLocalDateTime(startDate), toEndLocalDateTime(endDate)))
                .collect(Collectors.toList());
    }

    private LocalDateTime toStartLocalDateTime(LocalDate ld) {
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), MIN.getHour(), MIN.getMinute());
    }

    private LocalDateTime toEndLocalDateTime(LocalDate ld) {
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), MAX.getHour(), MAX.getMinute());
    }
}


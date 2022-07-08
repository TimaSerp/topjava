package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.id(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getReferenceById(userId));
        return crudMealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    public Map<User, List<Meal>> getUserWithMealsById(int userId) {
        Map<User, List<Meal>> userWithMeals = new HashMap<>();
        User user = crudUserRepository.findById(userId)
                        .orElse(null);
        userWithMeals.put(user, crudMealRepository.getAll(userId));
        return userWithMeals;
    }

    @Override
    public Map<Meal, User> getMealWithUserById(int id, int userId) {
        Map<Meal, User> mealAndUser = new HashMap<>();
        User user = crudUserRepository.findById(userId)
                .orElse(null);
        Meal meal = get(id, userId);
        mealAndUser.put(meal, user);
        return mealAndUser;
    }


}

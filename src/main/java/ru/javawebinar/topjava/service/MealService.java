package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        checkExist(userId);
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkExist(userId);
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        checkExist(userId);
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<Meal> getAll(int userId) {
        checkExist(userId);
        return repository.getAll(userId);
    }

    public Meal update(int userId, Meal meal) {
        checkExist(userId);
        return checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    private void checkExist(Integer userId) {
        if (userId == null) {
            throw new NotFoundException("User is not authorized");
        }
    }
}
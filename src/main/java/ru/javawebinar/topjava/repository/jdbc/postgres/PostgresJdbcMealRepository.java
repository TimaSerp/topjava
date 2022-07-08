package ru.javawebinar.topjava.repository.jdbc.postgres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.jdbc.AbstractJdbcMealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class PostgresJdbcMealRepository extends AbstractJdbcMealRepository {
    public PostgresJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime parseDateTime(LocalDateTime dateTime) {
        return dateTime;
    }

    @Override
    public Map<User, List<Meal>> getUserWithMealsById(int userId) {
        return null;
    }

    @Override
    public Map<Meal, User> getMealWithUserById(int id, int userId) {
        return null;
    }
}

package ru.javawebinar.topjava.repository.jdbc.hsqldb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.jdbc.AbstractJdbcMealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class HsqldbJdbcMealRepository extends AbstractJdbcMealRepository {
    public HsqldbJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDate parseDateTime(LocalDateTime dateTime) {
        return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfYear());
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

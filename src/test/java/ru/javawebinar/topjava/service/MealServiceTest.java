package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test
    public void getAdminFood() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_1.getId(), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_1.getId(), USER_ID));
    }

    @Test
    public void deleteAdminFood() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_1.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        assertMatch(meals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), USER_MEAL_7, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAllNotFound() {
        assertMatch(service.getAll(GUEST_ID), new ArrayList<>());
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(10, USER_MEAL_1.getDateTime(), "NOT FOUND UPDATE", 1000), USER_ID));
    }

    @Test
    public void updateAdminFood() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(100010, LocalDateTime.of(2000, Month.MAY, 1, 1, 1), "NOT FOUND UPDATE", 1000), USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, USER_MEAL_1.getDateTime(), "duplicated", 1000), USER_ID));
    }
}
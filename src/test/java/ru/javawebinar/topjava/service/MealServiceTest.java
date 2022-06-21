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
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

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
        Meal meal = service.get(userMeal1.getId(), USER_ID);
        assertMatch(meal, userMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(userMeal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(userMeal1.getId(), USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void deleteForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 
                30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        assertMatch(meals, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getBetweenNull() {
        List<Meal> meals = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(meals, service.getAll(USER_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), userMeal7, userMeal6, userMeal5,
                userMeal4, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getAllNotFound() {
        assertMatch(service.getAll(NOT_FOUND_ID), new ArrayList<>());
    }

    @Test
    public void update() {
        service.update(MealTestData.getUpdated(), USER_ID);
        Meal updated = MealTestData.getUpdated();
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(NOT_FOUND_ID,
                userMeal1.getDateTime(), "NOT FOUND UPDATE", 1000), USER_ID));
    }

    @Test
    public void updateForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(ADMIN_MEAL_ID,
                LocalDateTime.of(2000, Month.MAY, 1, 1, 1), 
                "NOT FOUND UPDATE", 1000), USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Meal newMeal = MealTestData.getNew();
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, userMeal1.getDateTime(), "duplicated", 1000), USER_ID));
    }
}
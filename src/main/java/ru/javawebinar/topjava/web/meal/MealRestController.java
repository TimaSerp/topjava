package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);
    private final MealService service;
    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MAX;
    private LocalTime startTime = LocalTime.MIN;
    private LocalTime endTime = LocalTime.MAX;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return getTos(service.getAll(authUserId(), LocalDate.MIN, LocalDate.MAX), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered() {
        log.info("getAllFiltered");
        return getFilteredTos(service.getAll(authUserId(), startDate, endDate), authUserCaloriesPerDay(), startTime, endTime);
    }

    public void delete(int id) {
        log.info("delete");
        service.delete(authUserId(), id);
    }

    public Meal get(int id) {
        log.info("get");
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("save");
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public Meal update(Meal meal, int id) {
        log.info("save");
        assureIdConsistent(meal, id);
        return service.create(authUserId(), meal);
    }
}
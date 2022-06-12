package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);
    private final MealService service = new MealService(new InMemoryMealRepository());

    public List<MealTo> getAll() {
        log.info("getAll");
        return getTos(service.getAll(authUserId()), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllFiltered");
        return getAll().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), toLocalDateTime(startDate, startTime), toLocalDateTime(endDate, endTime)))
                .collect(Collectors.toList());
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

    private LocalDateTime toLocalDateTime(LocalDate ld, LocalTime lt) {
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), lt.getHour(), lt.getMinute());
    }

}
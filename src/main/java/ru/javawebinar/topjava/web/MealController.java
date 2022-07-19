package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping("/meals")
public class MealController extends AbstractController {
    private static final Logger log = getLogger(MealController.class);

    @Autowired
    private MealService service;

    @GetMapping(params = "action=delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:meals";
    }

    @GetMapping(params = "action=update")
    public String update(HttpServletRequest request) {
        log.info("update meal");
        final Meal meal = service.get(getId(request), SecurityUtil.authUserId());
        return setSaveAttribute(request, meal);
    }

    private String setSaveAttribute(HttpServletRequest request, Meal meal) {
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(params = "action=create")
    public String create(HttpServletRequest request) {
        log.info("create meal");
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        return setSaveAttribute(request, meal);
    }

    @PostMapping
    public String save(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            log.info("update {} for user {}", meal, userId);
            assureIdConsistent(meal, getId(request));
            service.update(meal, userId);
        } else {
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        return "redirect:meals";
    }

    @GetMapping(params = "action=filter")
    public String getAllFiltered(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        request.setAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    @GetMapping
    public String getAll(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        request.setAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

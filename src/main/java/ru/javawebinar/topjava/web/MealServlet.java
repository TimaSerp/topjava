package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "update":
            case "create":
                String id = request.getParameter("id");

                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));

                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                if (meal.isNew()) {
                    controller.create(meal);
                } else {
                    controller.update(meal, Integer.parseInt(id));
                }
                response.sendRedirect("meals");
                break;
            case "all":
                log.info("get all filtered post");
                request.setAttribute("controller", controller);
                controller.setStartDate(getFilterDate(request, "startDate"));
                controller.setEndDate(getFilterDate(request, "endDate"));
                controller.setStartTime(getFilterTime(request, "startTime"));
                controller.setEndTime(getFilterTime(request, "endTime"));
                response.sendRedirect("meals");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("controller", controller);
                if (controller.getStartDate() == null || controller.getEndDate() == null ||
                        controller.getStartTime() == null || controller.getEndTime() == null) {
                    log.info("get all not filtered");
                    request.setAttribute("meals", controller.getAll());
                } else {
                    log.info("get all filtered get");
                    request.setAttribute("meals", controller.getAllFiltered());
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                response.sendRedirect("meals");
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate getFilterDate(HttpServletRequest request, String param) {
        String date = request.getParameter(param);
        return date == null ? null : LocalDate.parse(request.getParameter(param));
    }

    private LocalTime getFilterTime(HttpServletRequest request, String param) {
        String time = request.getParameter(param);
        return time == null ? null : LocalTime.parse(request.getParameter(param));
    }
}

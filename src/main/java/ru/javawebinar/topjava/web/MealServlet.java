package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CollectionMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static java.time.LocalTime.MAX;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private MealDao md;

    @Override
    public void init() {
        md = new CollectionMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward;
        String action = request.getParameter("action");

        if (action == null) {
            forward = LIST_MEAL;
            request.setAttribute("meals", MealsUtil.filteredByStreams(md.getAll(),
                    LocalTime.of(0, 0), MAX, 2000));
        } else {
            switch (action) {
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    forward = INSERT_OR_EDIT;
                    request.setAttribute("meal", md.get(id));
                    request.setAttribute("status", "edit");
                    break;
                case "delete":
                    id = Integer.parseInt(request.getParameter("id"));
                    md.delete(id);
                    forward = LIST_MEAL;
                    request.setAttribute("meals", MealsUtil.filteredByStreams(md.getAll(),
                            LocalTime.of(0, 0), MAX, 2000));
                    break;
                case "insert":
                    forward = INSERT_OR_EDIT;
                    request.setAttribute("meal", new Meal(null, null, null, 0));
                    request.setAttribute("status", "insert");
                    break;
                default:
                    forward = INSERT_OR_EDIT;
            }
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Integer id = Objects.equals(request.getParameter("id"), "") ? null : Integer.parseInt(request.getParameter("id"));
        md.save(new Meal(id, ldt, description, calories));

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("meals", MealsUtil.filteredByStreams(md.getAll(),
                LocalTime.of(0, 0), MAX, 2000));
        view.forward(request, response);
    }
}

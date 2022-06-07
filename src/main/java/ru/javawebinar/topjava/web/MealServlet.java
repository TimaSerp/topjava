package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealController mc;

    public MealServlet() {
        super();
        mc = new CollectionController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        request.setCharacterEncoding("UTF-8");

        String forward;
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            mc.deleteMeal(id);
            forward = LIST_MEAL;
            request.setAttribute("meals", mc.getAllMeals());
        } else if (action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            forward = INSERT_OR_EDIT;
            request.setAttribute("meal", mc.getMeal(id));
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_MEAL;
            request.setAttribute("meals", mc.getAllMeals());
        } else if (action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
            request.setAttribute("meal", mc.getMeal(-1));
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        mc.addOrUpdateMeal(new Meal(ldt, description, calories));

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("meals", mc.getAllMeals());
        view.forward(request, response);
    }
}

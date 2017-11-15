package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private ProfileRestController controller;
    private ClassPathXmlApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(ProfileRestController.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        if (AuthorizedUser.getUser() != null)
            request.setAttribute("loginUserName", AuthorizedUser.getUser().getName());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("do post");


        if (AuthorizedUser.getUser() != null)
            req.setAttribute("loginUserName", AuthorizedUser.getUser().getName());


        String action = req.getParameter("action");
        if (action != null){
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (action.equals("login")){
                User byMail = controller.getByMail(email);
                if (byMail.getPassword().equals(password))
                    AuthorizedUser.setUser(byMail);
                req.getRequestDispatcher("/users.jsp").forward(req, resp);
            }

            if (action.equals("register")){
                try {
                    controller.getByMail(email);
                }catch (NotFoundException ex){
                    log.info(" user with email: {} already exist", email);
                    req.getRequestDispatcher("/users.jsp").forward(req, resp);
                }

                String userName = req.getParameter("userName");
                Integer cal = Integer.parseInt(req.getParameter("cal"));
                User user = new User(null, userName, email, password, cal, true, null);
                controller.create(user);
                req.getRequestDispatcher("/users.jsp").forward(req, resp);
            }
        }
    }


    @Override
    public void destroy() {
        if (appCtx!=null)
            appCtx.close();
    }
}

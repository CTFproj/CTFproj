package main;

import auth.AppLogout;
import auth.AuthConfigFactory;
import auth.LoginInfo;
import database.DataBaseHelp;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import model.JsonTransformer;
import model.SolvedTask;
import model.Task;
import model.Team;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.RequiresAuthenticationFilter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    private static void run() {
        System.setProperty("file.encoding", "UTF-8");
        port(8080);
        //template engine
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/"));
        freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

        staticFileLocation("/public");
        //settings for auth
        final Config config = new AuthConfigFactory(freeMarkerEngine).build();
        final Route callback = new CallbackRoute(config);

        LoginInfo loginInfo = new LoginInfo();

        get("/callback", callback);
        post("/callback", callback);

        //page with auth
        before("/client", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/tasks", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/logout", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/pass", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/team/*", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/admin/task", new RequiresAuthenticationFilter(config, "FormClient"));

        get("/logout", new AppLogout(config));
        post("/logout", new AppLogout(config));


        get("/rating", (req, res) -> {
            res.removeCookie("flag");
            res.removeCookie("user");
            HashMap<String, Object> map = new HashMap<>();
            List<Team> first_list = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_DESC_SQL).executeAndFetch(Team.class);
            map.put("list", first_list);
            List<Team> total_list = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_WITH_GUEST_DESC_SQL).executeAndFetch(Team.class);
            map.put("totallist", total_list);
            ModelAndView header = getHeader(map, req, res);
            return new ModelAndView(map, "templates/rating.ftl");
        }, freeMarkerEngine);

        get("/", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            res.removeCookie("flag");
            res.removeCookie("user");
            ModelAndView header = getHeader(map, req, res);

            return new ModelAndView(map, "templates/index.ftl");
        }, freeMarkerEngine);

        //Check flag
        HashMap<String, Object> helpMap = new HashMap<>();
        post("/pass", (req, res) -> {
            UserProfile userProfile = getUserProfile(req, res);
            int task_id = Integer.parseInt(req.queryMap().get("taskid").value());
            int team_id = Integer.parseInt(userProfile.getId());
            String check_flag = req.queryMap().get("flg").value().toLowerCase();
            List<SolvedTask> solvedTasks = DataBaseHelp.sql2o.createQuery("SELECT * FROM solve_task WHERE team_id = :team_id").addParameter("team_id", team_id).executeAndFetch(SolvedTask.class);
            List<Task> task = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TASK_BY_ID_SQL).addParameter("id", task_id).executeAndFetch(Task.class);
            String flag = task.get(0).getFlag().toLowerCase();
            int score = task.get(0).getScore();
            logger.info("PASS TASK with team_id: " + userProfile.getId() + " with task_id: " + task_id + " with input: '" + check_flag + "' but flag is: " + flag);
            if (check_flag.equals(flag)) {
                try {
                    DataBaseHelp.sql2o.createQuery(DataBaseHelp.INSERT_SQL).addParameter("team_id", team_id).addParameter("task_id", task_id).executeUpdate().close();
                    DataBaseHelp.sql2o.createQuery(DataBaseHelp.UPDATE_TEAM_SQL).addParameter("val", score).addParameter("id", team_id).executeUpdate().close();
                    return 1;
                } catch (Exception e) {
                    System.err.println(e);
                    return 1;
                }
            }
            return 0;
        });
        post("/checktask", (req, res) -> {
            try {
                UserProfile userProfile = getUserProfile(req, res);
                int team_id = Integer.parseInt(userProfile.getId());
                int task_id = Integer.parseInt(req.queryMap().get("taskid").value());
                List<SolvedTask> solvedTasks = DataBaseHelp.sql2o.createQuery("SELECT * FROM solve_task WHERE team_id = :team_id").addParameter("team_id", team_id).executeAndFetch(SolvedTask.class);
                for (SolvedTask task : solvedTasks) {
                    if (task_id == task.getTask_id()) {
                        return 1;
                    }
                }
            } catch (Exception ignored) {
            }
            return 0;
        });

        //Tasks for authorized users
        get(("/tasks"), (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            if (getUserProfile(req, res) != null) {
                UserProfile userProfile = getUserProfile(req, res);
                int id = Integer.parseInt(userProfile.getId());
                List<Task> task = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TASK_SQL).executeAndFetch(Task.class);
                List<Task> not_solve_task = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_NOT_SOLVE_SQL).addParameter("id", id).executeAndFetch(Task.class);
                map.put("task", task);
                map.put("not_solve_task", not_solve_task);
            }
            ModelAndView header = getHeader(map, req, res);
            map.put("incorrect", helpMap.get("incorrect"));
            map.put("id", helpMap.get("id"));
            helpMap.remove("incorrect");
            helpMap.remove("id");
            return new ModelAndView(map, "templates/tasks.ftl");
        }, freeMarkerEngine);

        get("/login", (req, res) -> form(config.getClients(), req, res, loginInfo), freeMarkerEngine);

        //get profile info

        get("/client", (req, res) -> {
            res.removeCookie("flag");
            res.removeCookie("user");
            HashMap<String, Object> map = new HashMap<>();
            try {
                UserProfile user = getUserProfile(req, res);
                int id = Integer.parseInt(user.getId());
                List<Team> list = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_SQL).addParameter("id", id).executeAndFetch(Team.class);
                List<Task> task = DataBaseHelp.sql2o.createQuery("SELECT id,name,des,flag,score,category FROM task JOIN solve_task ON task.id = solve_task.task_id WHERE team_id = :id").addParameter("id", id).executeAndFetch(Task.class);
                int place = getPlace(list, id);
                if (list.size() > 0) {
                    map.put("name", list.get(0).getName());
                    map.put("place", place);
                    map.put("score", list.get(0).getScore());
                    map.put("id", id);
                }
                if (task.size() > 0) {
                    map.put("task", task);
                }
                ModelAndView header = getHeader(map, req, res);
                return new ModelAndView(map, "/templates/client.ftl");
            } catch (Exception e) {
                ModelAndView header = getHeader(map, req, res);
                return new ModelAndView(map, "/templates/client.ftl");
            }
        }, freeMarkerEngine);

        get("/team/:teamname", (req, res) -> {
            res.removeCookie("flag");
            res.removeCookie("user");
            HashMap<String, Object> map = new HashMap<>();
            String teamname = req.params("teamname");
            List<Team> list = DataBaseHelp.sql2o.createQuery("SELECT * FROM team WHERE name = :name").addParameter("name", teamname).executeAndFetch(Team.class);
            if (list.size() > 0) {
                int id = list.get(0).getId();
                List<Task> task = DataBaseHelp.sql2o.createQuery("SELECT id,name,des,flag,score,category FROM task JOIN solve_task ON task.id = solve_task.task_id WHERE team_id = :id").addParameter("id", id).executeAndFetch(Task.class);
                int place = getPlace(list, id);
                map.put("teamname", teamname);
                map.put("teamplace", place);
                map.put("teamscore", list.get(0).getScore());
                map.put("teamid", id);
                if (task.size() > 0) {
                    map.put("task", task);
                }
            }

            ModelAndView header = getHeader(map, req, res);
            return new ModelAndView(map, "/templates/team.ftl");
        }, freeMarkerEngine);

        get("/admin/task", "application/json", (request, response) -> DataBaseHelp.sql2o.createQuery("SELECT id,name,des,score,category FROM task ORDER BY category").executeAndFetch(Task.class), new JsonTransformer());
    }

    //settings
    public static void main(String[] args) {
        run();
    }
    //help methods

    //login
    private static ModelAndView form(final Clients clients, Request req, Response res, LoginInfo loginInfo) {
        final HashMap<String, Object> map = new HashMap<>();
        final FormClient formClient = clients.findClient(FormClient.class);
        String error = loginInfo.getError();
        map.put("callbackUrl", formClient.getCallbackUrl());
        map.put("title", "Авторизация");
        map.put("error", error);
        loginInfo.setError(null);
        return new ModelAndView(map, "templates/login.ftl");
    }

    //get profile of auth user
    private static UserProfile getUserProfile(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager manager = new ProfileManager(context);
        return manager.get(true);
    }

    //header
    private static ModelAndView getHeader(HashMap<String, Object> map, Request req, Response res) {
        res.removeCookie("flag");
        res.removeCookie("user");
        try {
            int id = Integer.parseInt(getUserProfile(req, res).getId());
            map.put("session", id);
            List<Team> list = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_SQL).addParameter("id", id).executeAndFetch(Team.class);
            Team team = list.get(0);
            int place = getPlace(list, id);
            map.put("name", team.getName());
            map.put("score", team.getScore());
            map.put("place", place);
        } catch (NullPointerException ignored) {
        }
        String path = req.pathInfo().substring(1);
        if (path.equals("client")) {
            path = "Профиль";
        }
        if (path.equals("rating")) {
            path = "Рейтинг";
        }
        if (path.equals("tasks")) {
            path = "Задания";
        }
        if (path.startsWith("team")) {
            path = "Команда - " + path.substring(5);
        }
        if (path.equals("")) {
            path = "Главная";
        } else {
            path = path.substring(0, 1).toUpperCase() + path.substring(1).replace("/", " - ");
        }
        map.put("title", path);
        return new ModelAndView(map, "templates/header.ftl");
    }

    //place
    private static int getPlace(List<Team> list, int id) {
        List<Team> rate = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_DESC_SQL).executeAndFetch(Team.class);
        int place = 0;
        for (int i = 0; i < rate.size(); i++) {
            if (rate.get(i).getId() == id) {
                place = i + 1;
                break;
            }
        }
        if (place == 0) {
            rate = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TEAM_WITH_GUEST_DESC_SQL)
                    .executeAndFetch(Team.class);
            for (int i = 0; i < rate.size(); i++) {
                if (rate.get(i).getId() == id) {
                    place = i + 1;
                    break;
                }
            }
        }
            return place;
        }

        //solved tasks
        private static List<Task> getSolvdeTask () {
            List<Task> tasks = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TASK_SQL).executeAndFetch(Task.class);
            List<Task> not_solved_tasks = DataBaseHelp.sql2o.createQuery(DataBaseHelp.SELECT_TASK_SQL).executeAndFetch(Task.class);
            ArrayList<Task> solved = new ArrayList<>();
            for (Task x : tasks) {
                for (Task y : not_solved_tasks) {
                    if (x.equals(y)) {
                        solved.add(x);
                    }
                }
            }
            return solved;
        }
    }
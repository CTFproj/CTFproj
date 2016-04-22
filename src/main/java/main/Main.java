package main;

import auth.AppLogout;
import auth.AuthConfigFactory;
import database.DataBaseHelp;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
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
import java.util.Set;

import static spark.Spark.*;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    //settings
    public static void main(String[] args) {
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

        DataBaseHelp db = new DataBaseHelp();

        get("/callback", callback);
        post("/callback", callback);

        //page with auth

        before("/client", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/tasks", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/logout", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/pass", new RequiresAuthenticationFilter(config, "FormClient"));

        get("/logout", new AppLogout(config));
        post("/logout", new AppLogout(config));


        get("/rating", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            List<Team> list = db.sql2o.open().createQuery(db.SELECT_TEAM_DESC_SQL).executeAndFetch(Team.class);
            map.put("list", list);
            ModelAndView header = getHeader(map,req,res,db);
            return new ModelAndView(map, "templates/rating.ftl");
        }, freeMarkerEngine);

        get("/", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();

            ModelAndView header = getHeader(map,req,res,db);

            return new ModelAndView(map, "templates/index.ftl");
        }, freeMarkerEngine);

        //Check flag
        post("/pass", (req, res) -> {

            Set<String> set =  req.queryParams();
            ArrayList<String> list = new ArrayList<>(set);
            String par = list.get(1);
            int task_id = Integer.parseInt(par);
            int team_id = Integer.parseInt(getUserProfile(req,res).getId());
            String val = req.queryParams(par).toLowerCase();

            List<Task> task = db.sql2o.open().createQuery(db.SELECT_TASK_BY_ID_SQL).addParameter("id", task_id).executeAndFetch(Task.class);
            String flag = task.get(0).getFlag();
            int score = task.get(0).getScore();

            if (val.equals(flag)) {
                try { db.sql2o.open().createQuery(db.INSERT_SQL).addParameter("team_id",team_id).addParameter("task_id",task_id).executeUpdate();
                    db.sql2o.open().createQuery(db.UPDATE_TEAM_SQL).addParameter("val",score).addParameter("id",team_id).executeUpdate();
                }  catch (Exception e) {
                    System.out.println(e);
                }
            }
            res.redirect("/tasks");
            return "";
        });

        //Tasks for authorized users
        get(("/tasks"), (req, res) -> {

            HashMap<String, Object> map = new HashMap<>();
            if (getUserProfile(req, res) != null) {
                UserProfile userProfile = getUserProfile(req, res);
                int id = Integer.parseInt(userProfile.getId());
                List<Task> task = db.sql2o.open().createQuery(db.SELECT_TASK_SQL).executeAndFetch(Task.class);
                List<Task> not_solve_task = db.sql2o.open().createQuery(db.SELECT_NOT_SOLVE_SQL).addParameter("id", id).executeAndFetch(Task.class);


                map.put("task", task);
                map.put("not_solve_task", not_solve_task);
        }
                ModelAndView header = getHeader(map,req,res,db);
                return new ModelAndView(map, "templates/tasks.ftl");
        },freeMarkerEngine );

        get("/login", (req,res) -> form(config.getClients(), req), freeMarkerEngine);

        //get profile info
        get("/client", (req, res) -> {
            UserProfile user = getUserProfile(req,res);
            try {
                int id = Integer.parseInt(user.getId());
                HashMap<String, Object> map = new HashMap<>();

                List<Team> list = db.sql2o.open().createQuery(db.SELECT_TEAM_SQL).addParameter("id",id).executeAndFetch(Team.class);
                int place = getPlace(db,list,id);

                map.put("id", id);
                map.put("name", user.getAttribute("username"));
                map.put("place", place);
                map.put("score", list.get(0).getScore());

                ModelAndView header = getHeader(map,req,res,db);
                return new ModelAndView(map,"/templates/" +
                        "client.ftl");
            } catch (NullPointerException e) {
                return null;
            }
        }, freeMarkerEngine);

    }

    //help methods

    //login
    private static ModelAndView form(final Clients clients, Request req) {
        final HashMap<String,Object> map = new HashMap<>();
        final FormClient formClient = clients.findClient(FormClient.class);
        map.put("title", "Login");
        map.put("callbackUrl", formClient.getCallbackUrl());
        return new ModelAndView(map, "templates/login.ftl");
    }
    //get profile of auth user
    private static UserProfile getUserProfile(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager manager = new ProfileManager(context);
        return manager.get(true);
    }
    //header
    private static ModelAndView getHeader (HashMap<String,Object> map, Request req, Response res, DataBaseHelp db) {
        try {
            int id = Integer.parseInt(getUserProfile(req,res).getId());
            map.put("session",id);
            List<Team> list = db.sql2o.open().createQuery(db.SELECT_TEAM_SQL).addParameter("id",id).executeAndFetch(Team.class);
            Team team = list.get(0);
            int place = getPlace(db,list,id);
            map.put("name",team.getName());
            map.put("score",team.getScore());
            map.put("place", place);
        }
        catch (NullPointerException e) {}
        String path = req.pathInfo().substring(1);
        if(path.equals("")) {
            path = "Main";
        } else {
            path = path.substring(0,1).toUpperCase() + path.substring(1);

        }
        map.put("title", path);
        ModelAndView header = new ModelAndView(map, "templates/header.ftl");
        return header;
    }
    //palace
    private static int getPlace(DataBaseHelp db, List<Team> list, int id) {
        List<Team> rate = db.sql2o.open().createQuery(db.SELECT_TEAM_DESC_SQL).executeAndFetch(Team.class);
        int place = 0;
        for(int i = 0; i < rate.size(); i++) {
            if(rate.get(i).getId() == id) {
                place = i + 1;
                break;
            }
        }
        return place;
    }
}
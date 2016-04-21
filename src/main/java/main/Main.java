package main;

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
import org.pac4j.sparkjava.ApplicationLogoutRoute;
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

import java.util.*;

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
        before("/team_info", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/client", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/flag", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/logout", new RequiresAuthenticationFilter(config, "FormClient"));
        before("/pass", new RequiresAuthenticationFilter(config, "FormClient"));

        post("/logout", new ApplicationLogoutRoute(config));
        get("/team_info", (req, res) -> {
            HashMap<String, Object> model = new HashMap();
            int id = Integer.parseInt(req.queryParams("id"));
            List<Team> list = DBSelect(db, id);
            model.put("name", list.get(0).getName());
            model.put("score", list.get(0).getScore());
            return new ModelAndView(model, "templates/team_prof.ftl");
        }, freeMarkerEngine);

        get("/rate", (req, res) -> {
            HashMap<String, Object> model = new HashMap();
            List<Team> list = db.sql2o.open().createQuery("SELECT * FROM team ORDER BY score DESC").executeAndFetch(Team.class);
            model.put("list", list);
            return new ModelAndView(model, "templates/rate.ftl");
        }, freeMarkerEngine);

        get("/", (req, res) -> {
            HashMap<String, Object> model = new HashMap();
            return new ModelAndView(model, "templates/index.ftl");
        }, freeMarkerEngine);
        post("/pass", (req, res) -> {

            Set<String> set =  req.queryParams();
            ArrayList<String> list = new ArrayList<>(set);
            String par = list.get(1);
            int task_id = Integer.parseInt(par);
            int team_id = Integer.parseInt(getUserProfile(req,res).getId());
            String val = req.queryParams(par).toLowerCase();

            List<Task> task = db.sql2o.open().createQuery("SELECT * FROM task WHERE id = :id").addParameter("id", task_id).executeAndFetch(Task.class);
            String flag = task.get(0).getFlag();
            int score = task.get(0).getScore();

            if (val.equals(flag)) {
                try { db.sql2o.open().createQuery("INSERT INTO solve_task(team_id, task_id) VALUES ( :team_id, :task_id)").addParameter("team_id",team_id).addParameter("task_id",task_id).executeUpdate();
                    db.sql2o.open().createQuery(db.UPDATE_SQL).addParameter("val",score).addParameter("id",team_id).executeUpdate();
                }  catch (Exception e) {
                    System.out.println(e);
                }
            }
            res.redirect("/flag");
            return "";
        });

        get(("/flag"), (req, res) -> {

            HashMap<String, Object> map = new HashMap<>();
            if (getUserProfile(req, res) != null) {
                UserProfile userProfile = getUserProfile(req, res);
                int id = Integer.parseInt(userProfile.getId());
                System.out.println(id);
                List<Task> task = db.sql2o.open().createQuery("SELECT * FROM task").executeAndFetch(Task.class);
                List<Task> not_solve_task = db.sql2o.open().createQuery("SELECT * FROM task EXCEPT\n" +
                        "(SELECT task.id, task.name, task.des, task.flag, task.score, task.category FROM solve_task\n" +
                        "  JOIN task ON solve_task.task_id = task.id WHERE team_id = :id)").addParameter("id", id).executeAndFetch(Task.class);

                map.put("task", task);
                map.put("not_solve_task", not_solve_task);
        }
                return new ModelAndView(map, "templates/flag.ftl");
        },freeMarkerEngine );

        get("/login", (req,res) -> form(config.getClients()), freeMarkerEngine);

        get("/client", (req, res) -> {
            UserProfile user = getUserProfile(req,res);
            try {
                int id = Integer.parseInt(user.getId());
                return "id = " + user.getId() + " teamname = " + user.getAttribute("username")
                        + "<p> <form action=\"/logout\" method=\"POST\">\n" +
                        "    <input id=\"btn_login\" name=\"btn_login\" type=\"submit\" class=\"btn btn-default\" value=\"Logout\" />\n" +
                        "</form></p>";
            } catch (NullPointerException e) {
                return null;
            }
        });

    }

    //help methods

    //Select from db
    public static List<Team> DBSelect(DataBaseHelp db, int id) {
        List<Team> list = db.sql2o.open().createQuery(db.SELECT_SQL).addParameter("id", id).executeAndFetch(Team.class);
        return list;
    }

    //login
    private static ModelAndView form(final Clients clients) {
        final Map map = new HashMap();
        final FormClient formClient = clients.findClient(FormClient.class);
        map.put("callbackUrl", formClient.getCallbackUrl());
        return new ModelAndView(map, "templates/login.ftl");
    }
    //get profile of auth user
    private static UserProfile getUserProfile(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager manager = new ProfileManager(context);
        return manager.get(true);
    }
}
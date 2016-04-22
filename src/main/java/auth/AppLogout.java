package auth;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Request;
import spark.Response;
import spark.Route;

public class AppLogout implements Route {
    private Config config;

    public AppLogout(Config config) {
        this.config = config;
    }

    public Object handle(Request request, Response response) throws Exception {
        CommonHelper.assertNotNull("config", this.config);
        SparkWebContext context = new SparkWebContext(request, response, this.config.getSessionStore());
        ProfileManager manager = new ProfileManager(context);
        manager.logout();
        response.redirect("/");

        return null;
    }
}



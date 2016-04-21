package auth;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;

import static spark.Spark.halt;

public class AuthHttpActionAdapter extends DefaultHttpActionAdapter {

    private final FreeMarkerEngine freeMarkerEngine;

    public AuthHttpActionAdapter(final FreeMarkerEngine freeMarkerEngine) {
        this.freeMarkerEngine = freeMarkerEngine;
    }

    @Override
    public Object adapt(int code, WebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, freeMarkerEngine.render(new ModelAndView(new HashMap<>(), "/templates/index.ftl")));
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, freeMarkerEngine.render(new ModelAndView(new HashMap<>(), "/templates/index.ftl")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}

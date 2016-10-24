package auth;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import spark.template.freemarker.FreeMarkerEngine;

public class AuthConfigFactory implements ConfigFactory {

    private final FreeMarkerEngine freeMarkerEngine;

    public AuthConfigFactory(final FreeMarkerEngine freeMarkerEngine) {
        this.freeMarkerEngine = freeMarkerEngine;
    }

    @Override
    public Config build() {
        // HTTP
        Authenticator auth = new Authenticator();
        final FormClient formClient = new FormClient("/login", auth);
        final IndirectBasicAuthClient indirectBasicAuthClient = new IndirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
        final Clients clients = new Clients("/callback", formClient, indirectBasicAuthClient);
        final Config config = new Config(clients);
        config.setHttpActionAdapter(new AuthHttpActionAdapter(freeMarkerEngine));
        return config;
    }
}

package auth;

import database.DataBaseHelp;
import model.Team;
import org.apache.commons.codec.digest.DigestUtils;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.credentials.UsernamePasswordCredentials;
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;
import org.pac4j.http.profile.HttpProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Authenticator implements UsernamePasswordAuthenticator {

    protected static final Logger logger = LoggerFactory.getLogger(Authenticator.class);
    private DataBaseHelp DB = new DataBaseHelp();

    public void validate(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            this.throwsException("No credential");
        }
        LoginInfo li = new LoginInfo();
        String username = credentials.getUsername();
        String password = DigestUtils.md5Hex(credentials.getPassword());
        logger.info("LOGIN with login: " + username + " and password: " + credentials.getPassword());
        List<Team> list = DataBaseHelp.sql2o.open()
                .createQuery("SELECT * FROM team WHERE name = :username")
                .addParameter("username", username)
                .executeAndFetch(Team.class);
        if (list.size() == 0) {
            li.setError("Enter a valid login");
            this.throwsException("Username cannot be blank");
        }

        String checkerPass = list.get(0).getPass();
        if (CommonHelper.isBlank(username)) {
            this.throwsException("Username cannot be blank");
        }

        if (CommonHelper.isBlank(password)) {
            this.throwsException("Password cannot be blank");
        }

        if (!password.equals(checkerPass)) {
            li.setError("Enter a valid password");
            this.throwsException("Username : \'" + username + "\' does not match password");
        }

        HttpProfile profile = new HttpProfile();
        profile.setId(list.get(0).getId());
        profile.addAttribute("username", username);
        credentials.setUserProfile(profile);

    }

    private void throwsException(String message) {
        throw new CredentialsException(message);
    }
}

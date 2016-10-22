package auth;

public class LoginInfo {
    private static String error;

    public void setError(String error) {
        LoginInfo.error = error;
    }

    public String getError() {
        return error;
    }
}

package auth;

public class LoginInfo {
    private static String error;

    void setError(String error) {
        LoginInfo.error = error;
    }

    public String getError() {
        return error;
    }
}

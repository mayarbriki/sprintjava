package services;

public class UserSessionService {
    private static UserSessionService instance;
    private int loggedInUserId;

    private UserSessionService() {}

    public static UserSessionService getInstance() {
        if (instance == null) {
            instance = new UserSessionService();
        }
        return instance;
    }

    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }
}

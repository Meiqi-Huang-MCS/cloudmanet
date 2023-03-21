public enum SuccessStatus {

    MANET_CREATED("Manet created successfully"),
    LOGIN_SUCCESSFUL("Login successful"),
    LOGOUT_SUCCESSFUL("Logout successful");

    private final String message;

    SuccessStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

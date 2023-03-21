public enum ResponseCode {
	SUCCESS(200, "Success"),
	ERROR(400, "Error"),
	LOGIN_SUCCESS(201, "Login successful"),
	LOGIN_ERROR(401, "Invalid username or password"),
	REGISTER_SUCCESS(202, "Registration successful"),
	REGISTER_ERROR(402, "Registration failed"),
	LOGOUT_SUCCESS(203, "Logout successful"),
	LOGOUT_ERROR(403, "Logout failed"),
	CREATE_MANET_SUCCESS(204, "Manet created successfully"),
	CREATE_MANET_ERROR(404, "Manet creation failed"),
	JOIN_MANET_SUCCESS(205, "Manet joined successfully"),
	JOIN_MANET_ERROR(405, "Manet join failed"),
	LEAVE_MANET_SUCCESS(206, "Manet left successfully"),
	LEAVE_MANET_ERROR(406, "Manet leave failed");

	private final int code;
	private final String message;

	ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}

package soundcloud4pms;

public class User {
	private String userName;
	private String avatarUrl;
	
	public User(String userName, String avatarUrl) {
		super();
		this.userName = userName;
		this.avatarUrl = avatarUrl.isEmpty() ? null : avatarUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
}

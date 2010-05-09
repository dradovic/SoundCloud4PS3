package soundcloud4ps3;

public class User extends Entity {
	private final int id;
	private final String userName;
	private final String avatarUrl;
	
	public User(int id, String userName, String avatarUrl) {
		super();
		this.id = id;
		this.userName = userName;
		this.avatarUrl = avatarUrl.isEmpty() ? null : avatarUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public String getFavoritesResource() {
		return String.format("users/%d/favorites", id);
	}
}

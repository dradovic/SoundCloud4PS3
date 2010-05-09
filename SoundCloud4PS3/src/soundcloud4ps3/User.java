package soundcloud4ps3;

public class User implements Comparable<User> {
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

	public String getTracksResource() {
		return String.format("users/%d/tracks", id);
	}

	public String getFavoritesResource() {
		return String.format("users/%d/favorites", id);
	}

	public String getFollowingsResource() {
		return String.format("users/%d/followings", id);
	}

	public String getFollowersResource() {
		return String.format("users/%d/followers", id);
	}

	@Override
	public int compareTo(User o) {
		return getUserName().compareTo(o.getUserName());
	}
}

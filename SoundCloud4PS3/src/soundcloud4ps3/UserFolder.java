package soundcloud4ps3;

class UserFolder extends CloudFolder {
	public UserFolder(User user, Cloud cloud) {
		super(user.getUserName(), user.getAvatarUrl(), user.getFavoritesResource(), cloud);
	}
	
	
}

package soundcloud4ps3;

import net.pms.dlna.virtual.VirtualFolder;

class UserFolder extends VirtualFolder {
	
	private final User user;
	private final Cloud cloud;
	
	public UserFolder(User user, Cloud cloud) {
		super(user.getUserName(), user.getAvatarUrl());
		this.user = user;
		this.cloud = cloud;
	}
	
	@Override
	public void discoverChildren() {
		super.discoverChildren();
		addChild(new CloudFolder("Tracks", null, user.getTracksResource(), cloud));
		addChild(new CloudFolder("Sets", null, user.getSetsResource(), cloud));
		addChild(new CloudFolder("Favorites", null, user.getFavoritesResource(), cloud));
		addChild(new CloudFolder("Followings", null, user.getFollowingsResource(), cloud));
		addChild(new CloudFolder("Followers", null, user.getFollowersResource(), cloud));
	}
}

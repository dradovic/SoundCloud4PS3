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
		addChild(new ResourceFolder("Tracks", null, user.getTracksResource(), cloud));
		addChild(new ResourceFolder("Sets", null, user.getSetsResource(), cloud));
		addChild(new ResourceFolder("Favorites", null, user.getFavoritesResource(), cloud));
		addChild(new ResourceFolder("Followings", null, user.getFollowingsResource(), cloud));
		addChild(new ResourceFolder("Followers", null, user.getFollowersResource(), cloud));
	}
}

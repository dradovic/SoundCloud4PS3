package soundcloud4ps3;

import net.pms.dlna.virtual.VirtualFolder;
import net.pms.external.SoundCloud4PS3;

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
		SoundCloud4PS3.log("discoverChildren:UserFolder"); // TODO: remove
		addChild(new CloudFolder("Favorites", null, user.getFavoritesResource(), cloud));
		addChild(new CloudFolder("Followings", null, user.getFollowingsResource(), cloud));
	}

}

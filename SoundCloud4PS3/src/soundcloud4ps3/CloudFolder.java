package soundcloud4ps3;

import java.util.ArrayList;

import net.pms.dlna.virtual.VirtualFolder;
import net.pms.external.SoundCloud4PS3;

public class CloudFolder extends VirtualFolder {

	private final ArrayList<ResourceNode> nodes;

	private Cloud cloud;

	public CloudFolder(String name, String thumbnailIcon, ArrayList<ResourceNode> nodes, Cloud cloud) {
		super(name, thumbnailIcon);
		assert cloud != null;
		
		this.cloud = cloud;
		this.nodes = nodes;
	}
	
	public CloudFolder(String name, ResourceNode node) {
		this(name, null, new ArrayList<ResourceNode>(), null);
		nodes.add(node);
	}
	
	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

//	public void setName(String name) {
//		this.name = name;
//	}
	
	@Override
	public void discoverChildren() {
		super.discoverChildren();
		SoundCloud4PS3.log("discoverChildren");
		if (cloud != null) {
			for (ResourceNode node : nodes) {
				User user = cloud.retrieveUser(node.getResource());
				addChild(new CloudFolder(user.getUserName(), user.getAvatarUrl(), node.getChildren(), cloud));
	//			addChild(new CloudFolder(user.getUserName() + "'s Follwings", "/user/followings", cloud));
			}
		}
	}
}

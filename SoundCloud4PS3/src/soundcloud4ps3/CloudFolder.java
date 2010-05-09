package soundcloud4ps3;

import net.pms.dlna.WebAudioStream;
import net.pms.dlna.virtual.VirtualFolder;
import net.pms.external.SoundCloud4PS3;

public class CloudFolder extends VirtualFolder {

	private final String resource;

	private Cloud cloud;

	public CloudFolder(String name, String thumbnailIcon, String resource, Cloud cloud) {
		super(name, thumbnailIcon);
		assert cloud != null;
		
		this.cloud = cloud;
		this.resource = resource;
	}
	
	public CloudFolder(String name, String resource) {
		this(name, null, resource, null);
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
		SoundCloud4PS3.log("discoverChildren"); // TODO: remove
		if (cloud != null) {
			for (Entity entity : cloud.retrieveEntities(resource)) {
				if (entity instanceof User) {
					User user = (User)entity;
					addChild(new UserFolder(user, cloud));
				}
				else if (entity instanceof Track)
				{
					Track track = (Track)entity;
					addChild(new WebAudioStream(track.getTitle(), track.getStreamUrl(), track.getArtworkUrl()));
				}
			}
		}
	}
}

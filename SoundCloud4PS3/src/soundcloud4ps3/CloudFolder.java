package soundcloud4ps3;

import java.util.ArrayList;
import java.util.Collections;

import net.pms.dlna.WebAudioStream;
import net.pms.dlna.virtual.VirtualFolder;

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

	@Override
	public void discoverChildren() {
		super.discoverChildren();
		if (cloud != null) {
			ArrayList<User> users = new ArrayList<User>();
			ArrayList<Track> tracks = new ArrayList<Track>();
			cloud.retrieveEntities(resource, users, tracks);
			Collections.sort(users);
			for (User user : users) {
				addChild(new UserFolder(user, cloud));
			}
			for (Track track : tracks) {
				addChild(new WebAudioStream(track.getTitle(), track.getStreamUrl(), track.getArtworkUrl()));
			}
		}
	}
}

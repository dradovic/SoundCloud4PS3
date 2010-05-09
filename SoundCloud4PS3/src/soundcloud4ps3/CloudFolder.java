package soundcloud4ps3;

import java.util.ArrayList;
import java.util.Collections;

import net.pms.dlna.WebAudioStream;
import net.pms.dlna.virtual.VirtualFolder;
import net.pms.external.SoundCloud4PS3;

public class CloudFolder extends VirtualFolder {

	private final String resource;

	private Cloud cloud;
	private boolean refresh;

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
		this.refresh = (this.cloud != cloud);
		this.cloud = cloud;
	}
	
	@Override
	public void discoverChildren() {
		super.discoverChildren();

		SoundCloud4PS3.logDebug("discoverChildren");
		addChildren();
	}

	private void addChildren() {
		if (cloud != null) {
			ArrayList<User> users = new ArrayList<User>();
			ArrayList<Track> tracks = new ArrayList<Track>();
			cloud.retrieveEntities(resource, users, tracks);
			Collections.sort(users);
			for (User user : users) {
				addChild(new UserFolder(user, cloud));
			}
			for (Track track : tracks) {
				addChild(new WebAudioStream(
					String.format("%s (%s)", track.getTitle(), track.getFormattedDuration()), 
					track.getStreamUrl(), 
					track.getArtworkUrl()));
			}
		}
		this.refresh = false;
	}
	
	@Override
	public boolean refreshChildren() {
		boolean refresh = this.refresh || super.refreshChildren();
		
		SoundCloud4PS3.logDebug("refreshChildren = %s", Boolean.toString(refresh));
		if (this.refresh) {
			addChildren();
		}
		return refresh;
	}
	
//	@Override
//	public boolean analyzeChildren(int count) {
//		SoundCloud4PS3.logDebug("analyzeChildren(%d)", count);
//		return super.analyzeChildren(count);
//	}
}

package soundcloud4ps3;

import java.util.ArrayList;

class Set {
	private final String title;
	private final String artworkUrl;
	private final ArrayList<Track> tracks;
	
	public Set(String title, String artworkUrl, ArrayList<Track> tracks) {
		this.title = title;
		this.artworkUrl = artworkUrl;
		this.tracks = tracks;
	}

	public String getTitle() {
		return title;
	}

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public ArrayList<Track> getTracks() {
		return tracks;
	}
}

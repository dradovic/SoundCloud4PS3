package soundcloud4ps3;

public class Track {
	private String title;
	private String artworkUrl;
	private String streamUrl;
	
	public Track(String title, String artworkUrl, String streamUrl) {
		super();
		assert !streamUrl.isEmpty();
		
		this.title = title;
		this.artworkUrl = artworkUrl.isEmpty() ? null : artworkUrl;
		this.streamUrl = streamUrl;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getArtworkUrl()
	{
		return artworkUrl;
	}

	public String getStreamUrl()
	{
		return streamUrl;
	}
}

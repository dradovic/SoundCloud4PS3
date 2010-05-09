package soundcloud4ps3;

public class Track {
	private String title;
	private int duration;
	private String artworkUrl;
	private String streamUrl;
	
	public Track(String title, int duration, String artworkUrl, String streamUrl) {
		super();
		assert !streamUrl.isEmpty();
		
		this.title = title;
		this.duration = duration;
		this.artworkUrl = artworkUrl.isEmpty() ? null : artworkUrl;
		this.streamUrl = streamUrl;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getFormattedDuration() {
		int total = duration/1000; // total in seconds
		int hours = total/3600;
		int minutes = (total/60) % 60;
		int seconds = total % 60;
		return String.format("%d:%02d:%02d", hours, minutes, seconds);
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

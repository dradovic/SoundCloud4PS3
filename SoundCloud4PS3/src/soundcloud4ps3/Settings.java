package soundcloud4ps3;

import net.pms.PMS;

public class Settings {
	private static final String SOUNDCLOUD4PS3_TOKEN = "soundcloud4ps3.token";
	private static final String SOUNDCLOUD4PS3_TOKEN_SECRET = "soundcloud4ps3.tokensecret";
	private static final String SOUNDCLOUD4PS3_DEBUG = "soundcloud4ps3.debug";

	public static String getToken()	{
		return (String)PMS.getConfiguration().getCustomProperty(SOUNDCLOUD4PS3_TOKEN);
	}

	public static void setToken(String token) {
		PMS.getConfiguration().setCustomProperty(SOUNDCLOUD4PS3_TOKEN, token);
	}

	public static String getTokenSecret() {
		return (String)PMS.getConfiguration().getCustomProperty(SOUNDCLOUD4PS3_TOKEN_SECRET);		
	}

	public static void setTokenSecret(String tokenSecret) {
		PMS.getConfiguration().setCustomProperty(SOUNDCLOUD4PS3_TOKEN_SECRET, tokenSecret);
	}
	
	public static boolean isDebug()	{
		String debug = (String)PMS.getConfiguration().getCustomProperty(SOUNDCLOUD4PS3_DEBUG);
		return debug != null ? Boolean.parseBoolean(debug) : false;
	}

	public static void setDebug(boolean debug) {
		PMS.getConfiguration().setCustomProperty(SOUNDCLOUD4PS3_DEBUG, Boolean.toString(debug));
	}
}

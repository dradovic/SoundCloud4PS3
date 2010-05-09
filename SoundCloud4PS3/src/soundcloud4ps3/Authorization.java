package soundcloud4ps3;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.urbanstew.soundcloudapi.SoundCloudAPI;
import org.urbanstew.soundcloudapi.SoundCloudAPI.State;

public class Authorization {
	private SoundCloudAPI api;
	private String authorizationUrl;

	public Authorization() {
		String token = Settings.getToken();
		String tokenSecret = Settings.getTokenSecret();
		if (token != null && tokenSecret != null) {
			api = new SoundCloudAPI("xECuntC8S1VOTtfxlGDdWA",
					"xey7LrTE6YJW9JZX8LR5AHZuQMDJ5SV010fYBYWH8s", token,
					tokenSecret);
		} else {
			api = new SoundCloudAPI("xECuntC8S1VOTtfxlGDdWA",
					"xey7LrTE6YJW9JZX8LR5AHZuQMDJ5SV010fYBYWH8s");
			updateAuthorizationUrl();
		}
	}

	private void updateAuthorizationUrl() {
		try {
			authorizationUrl = api.obtainRequestToken();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
	}

	public State getState() {
		return api.getState();
	}

	public Cloud getCloud() {
		if (isAuthorized()) {
			return new Cloud(api);
		} else {
			return null;
		}
	}

	public boolean isAuthorized() {
		return api.getState() == State.AUTHORIZED;
	}

	public String getAuthorizationUrl() {
		assert !isAuthorized();
		return authorizationUrl;
	}

	public void authorize(String verificationCode) {
		assert !isAuthorized();

		try {
			api.obtainAccessToken(verificationCode);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		Settings.setToken(api.getToken());
		Settings.setTokenSecret(api.getTokenSecret());

		assert isAuthorized();
	}

	public void unauthorize() {
		assert isAuthorized();

		api.unauthorize();

		Settings.setToken(null);
		Settings.setTokenSecret(null);

		updateAuthorizationUrl();

		assert !isAuthorized();
	}
}

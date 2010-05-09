package soundcloud4ps3;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.pms.external.SoundCloud4PS3;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.urbanstew.soundcloudapi.SoundCloudAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Cloud {
	private SoundCloudAPI api;
	
	private User user;
	private ArrayList<Track> favoriteTracks;

	public Cloud(SoundCloudAPI api)
	{
		this.api = api;
	}
	
	public User getUser()
	{
		if (user == null)
		{
			user = retrieveUser();
		}
		return user;
	}
	
	public ArrayList<Track> getFavoriteTracks()
	{
		if (favoriteTracks == null)
		{
			favoriteTracks = retrieveFavoriteTracks();
		}
		return favoriteTracks;
	}
	
	private ArrayList<Track> retrieveFavoriteTracks() {
		ArrayList<Track> tracks = new ArrayList<Track>();
		Document dom = retrieveDocument("me/favorites");
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			NodeList trackNodes = (NodeList) xpath.evaluate("/tracks/track",
					dom, XPathConstants.NODESET);
			for (int i = 0; i < trackNodes.getLength(); i++) {
				Node trackNode = trackNodes.item(i);
				String title = xpath.evaluate("title", trackNode);
				String artworkUrl = xpath.evaluate("artwork-url", trackNode);
				String streamUrl = xpath.evaluate("stream-url", trackNode);
				if (!streamUrl.isEmpty()) {
					tracks.add(new Track(title, artworkUrl, streamUrl));
				}
				else {
					SoundCloud4PS3.log("Warning: stream-url of track '%s' is empty", title);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return tracks;
	}
	
	public User retrieveUser(String resource)
	{
		Document dom = retrieveDocument(resource);
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String userName = xpath.evaluate("/user/username", dom);
			String avatarUrl = xpath.evaluate("/user/avatar-url", dom);
			return new User(userName, avatarUrl);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;		
	}

	private User retrieveUser() {
		return retrieveUser("me");
	}
	
	private Document retrieveDocument(String resource) {
		HttpResponse response = null;
		try {
			response = api.get(resource);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null && response.getStatusLine().getStatusCode() == 200) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document dom = db.parse(response.getEntity().getContent());
				return dom;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

//	private void WriteXmlFile(Document dom) {
//		try {
//			// Prepare the DOM document for writing
//			Source source = new DOMSource(dom);
//			// Prepare the output file
//			File file = new File("D:\\Temp\\test.xml");
//			Result result = new StreamResult(file); // Write the DOM document to
//			// the file
//			Transformer xformer = TransformerFactory.newInstance()
//					.newTransformer();
//			xformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//		} catch (TransformerException e) {
//		}
//	}
}

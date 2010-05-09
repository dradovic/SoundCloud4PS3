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
	
	public Cloud(SoundCloudAPI api)
	{
		this.api = api;
	}
	
	private Document retrieveDocument(String resource) {
		SoundCloud4PS3.log("Retrieving '%s'...", resource);
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
				Debugging.WriteXmlFile(dom, String.format("D:\\Temp\\%s.xml", resource.replace('/', '-'))); // TODO: remove
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

	public ArrayList<Entity> retrieveEntities(String resource) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		Document dom = retrieveDocument(resource);
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// extract Tracks
			NodeList trackNodes = (NodeList) xpath.evaluate("/tracks/track",
					dom, XPathConstants.NODESET);
			for (int i = 0; i < trackNodes.getLength(); i++) {
				Node trackNode = trackNodes.item(i);
				String title = xpath.evaluate("title", trackNode);
				String artworkUrl = xpath.evaluate("artwork-url", trackNode);
				String streamUrl = xpath.evaluate("stream-url", trackNode);
				if (!streamUrl.isEmpty()) {
					entities.add(new Track(title, artworkUrl, streamUrl));
				}
				else {
					SoundCloud4PS3.log("Warning: stream-url of track '%s' is empty", title);
				}
			}
			
			// extract single User
			NodeList userNodes = (NodeList) xpath.evaluate("/user",
					dom, XPathConstants.NODESET);
			for (int i = 0; i < userNodes.getLength(); i++) {
				Node userNode = userNodes.item(i);
				int id = Integer.parseInt(xpath.evaluate("id", userNode));
				String userName = xpath.evaluate("username", userNode);
				String avatarUrl = xpath.evaluate("avatar-url", userNode);
				entities.add(new User(id, userName, avatarUrl));
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return entities;
	}
}

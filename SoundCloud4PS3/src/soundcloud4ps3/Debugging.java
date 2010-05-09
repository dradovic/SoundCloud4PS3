package soundcloud4ps3;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

class Debugging {
	public static void WriteXmlFile(Document dom, String fileName) {
		try {
			// Prepare the DOM document for writing
			Source source = new DOMSource(dom);
			// Prepare the output file
			File file = new File(fileName);
			Result result = new StreamResult(file); // Write the DOM document to
			// the file
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		}
	}

}

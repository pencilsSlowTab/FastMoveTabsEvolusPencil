import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlMoveTool {
	
	 private File fXmlFile ;
	 private Document doc ;
	 NodeList nList;

	public XmlMoveTool(File fXmlFile) {
		super();
		this.fXmlFile = fXmlFile;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void save() {
		try {
			/* Save the editing */
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new FileOutputStream(fXmlFile));
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveUp(int i) {
		Node pageToMove = nList.item(i);
		moveUp(pageToMove);
		save();
	}

	private void moveUp(Node pageToMove) {
		pageToMove.getParentNode().insertBefore(pageToMove, pageToMove.getPreviousSibling());
	}

	
	public void moveDown(int i) {
		Node pageToMove = nList.item(i);
		moveDown(pageToMove);
		save();
	}
	
	private void moveDown(Node pageToMove) {
		Node nextSibling = pageToMove.getNextSibling();
		if (nextSibling != null && nextSibling.getNextSibling() != null) {
			pageToMove.getParentNode().insertBefore(pageToMove, nextSibling.getNextSibling());
		} else {
			pageToMove.getParentNode().insertBefore(pageToMove, null);
		}
	}

	public String[] list() throws Exception {
		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		nList = doc.getElementsByTagName("Page");
		String[] pagesNames = new String[nList.getLength()];

		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			// if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			NodeList properties = eElement.getElementsByTagName("Property");
			for (int i = 0; i < properties.getLength(); i++) {
				Node pNode = properties.item(i);
				Element pElement = (Element) pNode;
				String attribute = pElement.getAttribute("name");
				if (attribute.equals("name")) {
					pagesNames[temp]=pElement.getTextContent();
				}

			}
		}
		return pagesNames;
	}

}

package OpenSW;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {
	void makeIndex(String args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(args);

		Document doc2=docBuilder.newDocument(); //index.xml �����
		Element docs = doc2.createElement("docs"); 
		doc2.appendChild(docs);

		String titleText=""; 
		String bodyText="";
		
		Element root=doc.getDocumentElement(); //root element(docs)
		NodeList children=root.getChildNodes(); //  �ڽĳ���� doc

		for(int i=0;i<children.getLength();i++) { //length=5
			Node docNode=children.item(i); 	 

			if(docNode.getNodeType()==Node.ELEMENT_NODE) {

				//doc element
				Element docE=(Element)docNode;
				String id=docE.getAttribute("id");

				//title element
				NodeList titleList=docE.getElementsByTagName("title");
				Element titleE=(Element)titleList.item(0);
				titleText=titleE.getTextContent();

				//body element
				NodeList bodyList=docE.getElementsByTagName("body");
				Element bodyE=(Element)bodyList.item(0);
				bodyText=bodyE.getTextContent();

				//Ű���� ����
				String KeyString="";
				KeywordExtractor ke=new KeywordExtractor();
				KeywordList kl=ke.extractKeyword(bodyText,true);
				for(int j=0;j<kl.size();j++) {
					Keyword kwrd=kl.get(j);
					KeyString+=kwrd.getString()+":"+kwrd.getCnt()+"#";
				}

				//���ο� document element ����
		
				Element ndoc=doc2.createElement("doc");
				docs.appendChild(ndoc);
				//	String id = Integer.toString(i);
				ndoc.setAttribute("id", id);

				//title element
				Element title = doc2.createElement("title");
				title.appendChild(doc2.createTextNode(titleText));
				ndoc.appendChild(title);

				//body element 
				Element body = doc2.createElement("body");
				body.appendChild(doc2.createTextNode(KeyString));
				ndoc.appendChild(body);
			}


		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(doc2);  
		StreamResult result = new StreamResult(new FileOutputStream(new File("index.xml")));
		transformer.transform(source, result);

	}







}

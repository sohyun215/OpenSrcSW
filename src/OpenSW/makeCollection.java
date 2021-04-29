package OpenSW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	
	void makeCollection(String args) throws ParserConfigurationException, FileNotFoundException, TransformerException {
	//	File dir = new File("data");
		File dir = new File(args);
		String html = "";
		String str;
		String ptext="" ; //body내용
		String titletext = "";
		File[] file = dir.listFiles(); // 디

		//document 媛앹껜 �깮�꽦
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument(); 
		
		Element docs = doc.createElement("docs"); 
		doc.appendChild(docs);

		for (int i = 0; i < file.length; i++) {
			
			try {
				FileReader fr = new FileReader(file[i]);
				BufferedReader br = new BufferedReader(fr);
				while ((str = br.readLine()) != null) { // 臾몄꽌 �걹源뚯� �븳以꾩뵫 �떎 �씫�뼱�꽌 臾몄옄�뿴 ���옣
					str += "\n";
					html += str;
				}
				// System.out.println(html);
				org.jsoup.nodes.Document document = Jsoup.parse(html);

				for (org.jsoup.nodes.Element p : document.select("p")) {
					ptext += p.text(); // p�깭洹몃줈 媛먯떥吏� �궡�슜
				}
				for (org.jsoup.nodes.Element t : document.select("title")) {
					titletext = t.text(); //title �깭洹�
				}
				//doc element
				Element doct = doc.createElement("doc");
				docs.appendChild(doct);
				String id = Integer.toString(i);
				doct.setAttribute("id", id);
				
				//title element- title�깭洹� �냽 text
				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(titletext));
				doct.appendChild(title);

				//body element <p>�깭洹� �떎 �젣嫄고븳 �궡�슜留� 
				Element body = doc.createElement("body");
				body.appendChild(doc.createTextNode(ptext));
				doct.appendChild(body);

				html = "";
				ptext = "";

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} // for臾� �걹

		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc); //doc 媛앹껜 
		StreamResult result = new StreamResult(new FileOutputStream(new File("collection.xml")));
		transformer.transform(source, result);

	}
}

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

public class testamain {

	public static void main(String[] args)
			throws ParserConfigurationException, FileNotFoundException, TransformerException {
		// TODO Auto-generated method stub

		//String filepath = "C:\\Users\\kshyu\\OneDrive\\바탕 화면\\2주차 실습 html";
		File dir = new File("2주차 실습 html");
		String html = "";
		String str;
		String ptext="" ; //body내용 
		String titletext = "";
		File[] file = dir.listFiles(); // 디렉토리에 있는 파일들 배열로 반환

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Element docs = doc.createElement("docs"); 
		doc.appendChild(docs);

		for (int i = 0; i < file.length; i++) {
			
			try {
				FileReader fr = new FileReader(file[i]);
				BufferedReader br = new BufferedReader(fr);
				while ((str = br.readLine()) != null) { // 문서 끝까지 한줄씩 다 읽어서 문자열 저장
					str += "\n";
					html += str;
				}
				 System.out.println(html);
				org.jsoup.nodes.Document document = Jsoup.parse(html);

				for (org.jsoup.nodes.Element p : document.select("p")) {
					ptext += p.text(); // p태그로 감싸진 내용
				}
				for (org.jsoup.nodes.Element t : document.select("title")) {
					titletext = t.text(); //title 태그
				}
				//doc element
				Element doct = doc.createElement("doc");
				docs.appendChild(doct);
				String id = Integer.toString(i);
				doct.setAttribute("id", id);
				
				//title element- title태그 속 text
				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(titletext));
				doct.appendChild(title);

				//body element <p>태그 다 제거한 내용만 
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

		} // for문 끝

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("src/collection.xml")));
		transformer.transform(source, result);

	 }

}

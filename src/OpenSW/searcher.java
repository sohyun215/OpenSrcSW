package OpenSW;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class searcher {
	HashMap<String, Double> kwrdMap = new HashMap<>();
	searcher(String file, String query)
			throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {

		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for (int j = 0; j < kl.size(); j++) {
			Keyword kwrd = kl.get(j);
			kwrdMap.put(kwrd.getString(), (double) 1); // query의 keyword와 weight저장
		}
		printTitle(CalcSim(query, file));
	}


	HashMap<Integer, String> findTitle() throws ParserConfigurationException, SAXException, IOException {
		String xmlFilepath = "collection.xml";
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFilepath);

		Element root = doc.getDocumentElement(); // docs element
		NodeList childNodes = root.getChildNodes(); // doc
		int size = childNodes.getLength(); // 문서 개수 저장
		HashMap<Integer, String> title = new HashMap<>();
		for (int i = 0; i < size; i++) {
			Node docNode = childNodes.item(i);
			if (docNode.getNodeType() == Node.ELEMENT_NODE) {
				Element docE = (Element) docNode;
				Element docTitle = (Element) docE.getElementsByTagName("title").item(0);
				title.put(i, docTitle.getTextContent());
			}
		}
		return title;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	HashMap<Integer, Double> CalcSim(String query, String file){
		
		return null;
		
	}
	
	@SuppressWarnings("rawtypes")
	void printTitle(HashMap<Integer, Double> simMap) throws ParserConfigurationException, SAXException, IOException {
		HashMap title = findTitle();
		List<Integer> sortedList = new ArrayList<>(simMap.keySet()); // 문서id저장됨
		Collections.sort(sortedList, (o1, o2) -> (simMap.get(o2).compareTo(simMap.get(o1)))); //내림차순 정렬
		for (int i = 0; i < 3; i++) { // 상위 3위까지의 문서 title출력
			System.out.println(title.get(sortedList.get(i)));
		}

	}
}

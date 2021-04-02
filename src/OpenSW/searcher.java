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
	HashMap<Integer, Double> CalcSim(String query, String file)
			throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {

		FileInputStream fileIStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileIStream);

		Object object = objectInputStream.readObject();
		objectInputStream.close();
		HashMap hashMap = (HashMap) object;
		int docsize = findTitle().size(); // 문서 개수
		ArrayList<String> val = new ArrayList<>(); 
		HashMap<String, ArrayList> idMap = new HashMap<>(); //id, 문서에 있는 query의 가중치 저장

		for (String key : kwrdMap.keySet()) {
			val = (ArrayList<String>) hashMap.get(key); // post파일에서 query와 대응하는 id,가중치 가져옴
			for (int j = 0; j < docsize;) {
				for (int i = 0; i < val.size(); j++, i += 2) {
					ArrayList<String> weight = new ArrayList<>();
					if (val.contains(Integer.toString(j))) {
						if (idMap.containsKey(val.get(i))) {
							idMap.get(val.get(i)).add(val.get(i + 1));
						} 
						else {
							weight.add(val.get(i + 1));
							idMap.put(val.get(i), weight);
						}
					} 
					else { //문서 속 query의 keyword가 없는 경우 가중치 0으로 
						if (idMap.containsKey(Integer.toString(j))) {
							idMap.get(Integer.toString(j)).add("0");
						} else {
							weight.add("0");
							idMap.put(Integer.toString(j), weight);
							
						}
					}
				}

			}
		}

		HashMap<Integer, Double> simMap = new HashMap<>();
		double[] temp = new double[docsize];
		ArrayList<Double> wq = new ArrayList<>();
		for (String key : kwrdMap.keySet()) {
			wq.add(kwrdMap.get(key));
		}
		for (int i = 0; i < idMap.size(); i++) {
			for (int j = 0; j < idMap.get(Integer.toString(i)).size(); j++) {
				temp[i] += wq.get(j) * Double.parseDouble((String) idMap.get(Integer.toString(i)).get(j));
			}
			temp[i]= Math.round(temp[i] * 100) / 100.0; //소수점 셋째자리에서 반올림
			simMap.put(i, temp[i]);
		}

		return simMap;

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

package OpenSW;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	void makeInvertedFile(String file)
			throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file); // index.xml 불러오기

		Element root = doc.getDocumentElement(); // docs element
		NodeList childNodes = root.getChildNodes(); // doc
		int size = childNodes.getLength(); // 문서 개수 저장

		HashMap<String, ArrayList<String>> postMap = new HashMap<>();
		HashMap<String, Integer> df = new HashMap<>(); // 키워드마다 몇 개의 문서에 등장하는지

		for (int i = 0; i < size; i++) {
			Node docNode = childNodes.item(i);
			if (docNode.getNodeType() == Node.ELEMENT_NODE) {
				Element docE = (Element) docNode;
				String id = docE.getAttribute("id");
				Element body = (Element) docE.getElementsByTagName("body").item(0); 
				String[] str = body.getTextContent().split("#"); //body내용 #로 구분
				for (int j = 0; j < str.length; j++) {
					ArrayList<String> value = new ArrayList<>(); // hashmap에 value로 저장할 list
					String[] word = str[j].split(":");
					String kwd = word[0]; //keyword
					String fq = word[1];  //frequency
					if (df.containsKey(kwd) && postMap.containsKey(kwd)) { // 이미 나왔던 단어
						df.put(kwd, df.get(kwd) + 1); // df+1
						postMap.get(kwd).add(id);
						postMap.get(kwd).add(fq);

					} else {
						df.put(kwd, 1);
						value.add(id);
						value.add(fq); // 일단 빈도수 넣음
						postMap.put(kwd, value);

					}
				}

			}
		}
		for (String kwd : postMap.keySet()) {
			ArrayList<String> list = postMap.get(kwd); // id와 빈도수 저장되어 있음
			for (int i = 1; i < list.size(); i += 2) {// 빈도수로 저장한 값을 가중치 계산해서 바꿔줌
				int tf = Integer.parseInt(list.get(i));
				double weight = getWeight(size, tf, df.get(kwd));
				postMap.get(kwd).set(i, Double.toString(weight));

			}

		}
		// haspMap 객체를 파일에 저장
		FileOutputStream fileStream = new FileOutputStream("index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		objectOutputStream.writeObject(postMap);
		objectOutputStream.close();

		readInvertedFile();

	}

	double getWeight(int size, int tf, int df) { // 가중치 구하는 함수
		double weight = tf * Math.log((double) size / df); // double로 type casting
		weight = Math.round(weight * 100) / 100.0; // 소수점 셋째자리에서 반올림
		return weight;
	}

	void readInvertedFile() throws IOException, ClassNotFoundException {
		// 파일에서 객체 불러오기
		FileInputStream fileIStream = new FileInputStream("index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileIStream);

		Object object = objectInputStream.readObject();
		objectInputStream.close();

		System.out.println("읽어온 객체의 type -> " + object.getClass());
		HashMap hashMap = (HashMap) object;
		Iterator<String> it = hashMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			ArrayList val = (ArrayList) hashMap.get(key);
			System.out.println(key + "->" + val);
		}
	}
}

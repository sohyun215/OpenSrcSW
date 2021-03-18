package OpenSW;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		// TODO Auto-generated method stub

		if (args[1].equals("-c")) {
			makeCollection collection = new makeCollection();
			collection.makeCollection(args[2]);
		}
		if(args[1].equals("-k")) {
			makeKeyword kwrd=new makeKeyword();
			kwrd.makeIndex(args[2]);
		}
	}

}

package OpenSW;

import java.util.HashMap;
import java.util.Scanner;

public class genSnippet {
 genSnippet(String file, String kwrd){
	Scanner scan=new Scanner(file);
	String str = null;
	String kwd[];
	String search[]=kwrd.split(" ");
	HashMap map=new HashMap();
	int line=1;
	while(scan.hasNextLine()) {
		str=scan.nextLine();
		kwd=str.split(" ");
		map.put(line, kwd);
		line++;
	}
	for(int i=0;i<line;i++) {
		
	}
 }
}

package utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datamodel.Riga;

public class ParsingUtil {

	public static List<String> getWordList(String current){

		String[] temp = current.split("[\\s]");
		List<String> result = new ArrayList<>();

		for(String s: temp) {
			Matcher m = Pattern
					.compile("(ADJ|ADP|ADV|AUX|CCONJ|DET|INTJ|NOUN|NUM|PART|PRON|PROPN|PUNCT|SCONG|SYM|VERB)?(:)?(.*)")
					.matcher(s);
			result.add(m.replaceFirst("$3"));
		}
		return result;
	}

	public static List<String> getTagList(String current){

		String[] temp = current.split("[\\s]");
		List<String> result = new ArrayList<>();

		for(String s: temp) {
			Matcher m = Pattern
					.compile("(ADJ|ADP|ADV|AUX|CCONJ|DET|INTJ|NOUN|NUM|PART|PRON|PROPN|PUNCT|SCONG|SYM|VERB)?(:)?(.*)")
					.matcher(s);
			result.add(m.replaceFirst("$1"));
		}
		return result;
	}
	
	public static String getReparsed(List<String> tags, List<String> words) {
		if(tags == null || tags.isEmpty() || words==null || words.isEmpty()) {
			return null;
		}
		String result =null;
		
		int len = tags.size();
		if(len==words.size()) {
			result = tags.get(0)+(tags.get(0).equals("") ? "" : ":")+words.get(0);
			for(int i=1;i<len;i++) {
				result= result + " " + tags.get(i)+(tags.get(i).equals("") ? "" : ":")+words.get(i);
			}
		}
		return result;
	}

	public static String conc(String[] stringArray) {
		
		if(stringArray==null || stringArray.length==0) {
			return "";
		}
		String result=stringArray[0];

		for(int c = 1; c < stringArray.length; c++) {
			result = result +" "+stringArray[c];
		}
		return result;
	}
	
	public static Riga getById(List<Riga> lista, int id) {
		for(Riga r : lista) {
			if(r.getId()==id) {
				return r;
			}
		}
		return null;
	}

	public static String getId(int firstRiga,int secondRiga, int c){
		return String.valueOf(firstRiga)+"-"+String.valueOf(secondRiga)+"-"+String.valueOf(c);
	}
}

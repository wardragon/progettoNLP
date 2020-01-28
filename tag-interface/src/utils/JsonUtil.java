package utils;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import datamodel.Pair;
import datamodel.Riga;
import datamodel.Voto;

public class JsonUtil {
	
	public static ObjectMapper mapper = new ObjectMapper();
	
	public static List<Pair> retrieveCoupleList(String fileName, List<Riga> listaRighe) throws IOException {
		List<Pair> listaCoppie = new ArrayList<Pair>();
		
		InputStream is = null;
		BufferedReader buf = null;
		
		try {
			is = new FileInputStream(fileName); 
			buf = new BufferedReader(new InputStreamReader(is)); 
			
			String line = buf.readLine(); 
			while(line != null)
			{ 
				Matcher m = Pattern
						.compile("([0-9]*),([0-9]*)")
						.matcher(line);
				
				listaCoppie.add(new Pair(ParsingUtil.getById(listaRighe, Integer.parseInt(m.replaceFirst("$1"))), ParsingUtil.getById(listaRighe, Integer.parseInt(m.replaceFirst("$2")))));
				
				line = buf.readLine();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(buf!=null) {
				buf.close();
			}
			if(is!=null) {
				is.close();
			}
		}
		
		
		
		
		
		return listaCoppie;
	}

	public static List<Riga> retrieveRighe(String filePath) {
		try {

			FileInputStream is = new FileInputStream(filePath);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is,"ISO-8859-1"));
			String line = buf.readLine(); 
			StringBuilder sb = new StringBuilder(); 
			while(line != null)
			{ 
				sb.append(line).append("\n"); 
				line = buf.readLine();
			} 
			String jsonArrayString = sb.toString();
			buf.close();
			is.close();

			
			List<Riga> listOfRiga = mapper.readValue(jsonArrayString, new TypeReference<List<Riga>>() { });

			System.out.println(listOfRiga.size());
			
			return listOfRiga;

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;

	}
	
	public static List<Riga> retrieveRigheForRegen(String filePath) {
		try {

			FileInputStream is = new FileInputStream(filePath);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String line = buf.readLine(); 
			StringBuilder sb = new StringBuilder(); 
			while(line != null)
			{ 
				sb.append(line).append("\n"); 
				line = buf.readLine();
			} 
			String jsonArrayString = sb.toString();
			buf.close();
			is.close();

			
			List<Riga> listOfRiga = mapper.readValue(jsonArrayString, new TypeReference<List<Riga>>() { });

			System.out.println(listOfRiga.size());
			
			return listOfRiga;

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;

	}

//	public static void saveRigheOnFile2(List<Riga> listOfRiga,String filePath) {
//		try {
//			String updatedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOfRiga);
//
//			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
//			writer.write(updatedString);
//			writer.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void saveRigheOnFile(List<Riga> listOfRiga,String filePath) {
		Writer out= null;
		
		try {
			String updatedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOfRiga);

			out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(filePath), "ISO-8859-1"));
				
				    out.write(updatedString);
				
			

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		   if(out!=null) {
			   try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
		}		
	}
	
	public static List<Voto> retrieveVoti() {
		try {

			InputStream is = new FileInputStream("semantics.json"); 
			BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
			String line = buf.readLine(); 
			StringBuilder sb = new StringBuilder(); 
			while(line != null)
			{ 
				sb.append(line).append("\n"); 
				line = buf.readLine();
			} 
			String jsonArrayString = sb.toString();
			buf.close();
			is.close();

			List<Voto> listOfVoti= new ArrayList<>();
			if(jsonArrayString.length()>0) {
				listOfVoti = mapper.readValue(jsonArrayString, new TypeReference<List<Voto>>() { });
			}
			return listOfVoti;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;

	}
	
	
	
	public static void saveVotiOnFile(List<Voto> listOfVoto) {
		try {
			String updatedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOfVoto);

			BufferedWriter writer = new BufferedWriter(new FileWriter("semantics.json"));
			writer.write(updatedString);
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

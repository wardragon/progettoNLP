package dataProcessing;

import java.nio.charset.Charset;
import java.util.List;

import datamodel.Riga;
import utils.JsonUtil;

public class RegenerateCleanDataset {

	public static void main(String[] args) {

		List<Riga> listaRighe = JsonUtil.retrieveRigheForRegen("cleanOriginal.json");
		
		for(int i=0;i<listaRighe.size();i++) {
			listaRighe.get(i).setId(i);
		}
		
		System.out.println(System.getProperty("file.encoding"));
		
		JsonUtil.saveRigheOnFile(listaRighe, "dataset.json");
	}

}

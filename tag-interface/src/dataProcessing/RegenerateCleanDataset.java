package dataProcessing;

import java.util.List;

import datamodel.Riga;
import utils.JsonUtil;

public class RegenerateCleanDataset {

	public static void main(String[] args) {

		List<Riga> listaRighe = JsonUtil.retrieveRigheForRegen("cleanOriginal.json");
		
		for(int i=0;i<listaRighe.size();i++) {
			listaRighe.get(i).setId(i);
		}
		
		JsonUtil.saveRigheOnFile(listaRighe, "dataset.json");
	}

}

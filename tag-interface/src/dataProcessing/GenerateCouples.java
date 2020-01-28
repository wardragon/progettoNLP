package dataProcessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datamodel.Riga;
import utils.JsonUtil;

public class GenerateCouples {

	private static final int PARTECIPANTS = 10;
	private static final int COUPLES = 50;

	public static void main(String[] args) throws Exception {

		List<Riga> listaRighe = JsonUtil.retrieveRighe("dataset.json");

		int tempStart=0;

		int tempEnd=listaRighe.size();


		Map<String,List<Riga>> tempSet=new HashMap<>();

		for(int c=tempStart; c<tempEnd ; c++) {
			tempSet.put(listaRighe.get(c).getData(), new ArrayList<>());
		}
		for(int c=tempStart; c<tempEnd ; c++) {
			tempSet.get(listaRighe.get(c).getData()).add(listaRighe.get(c));
		}

		List<String> listaCoppie = new ArrayList<>();
		

		for (Map.Entry<String,List<Riga>> entry : tempSet.entrySet()) {
			List<Riga> currentList = entry.getValue();
			int currentCouples = currentList.size();
			if(currentCouples>1) {
				for(int i=0; i<currentCouples;i++) {
					for(int j=i; j<currentCouples;j++) {
						if(i!=j) {
							listaCoppie.add(currentList.get(i).getId()+","+currentList.get(j).getId());
						}
					}
				}
			}
		}
		
		int index=0;
		
		
		for(int p=0;p<PARTECIPANTS;p++) {
			BufferedWriter writer = new BufferedWriter(new FileWriter("listaRighe"+p));
			
			
			for(int coup=0;coup<COUPLES;coup++) {
				writer.write(listaCoppie.get(index)+"\n");
				index++;
			}
			
			writer.close();
		}

		JsonUtil.saveRigheOnFile(listaRighe, "dataset.json");
	}


}

package dataProcessing;

import java.io.IOException;
import java.util.List;

import datamodel.Pair;
import datamodel.Riga;
import utils.JsonUtil;

public class Test {

	public static void main(String[] args) throws IOException {
		List<Riga> listaRighe = JsonUtil.retrieveRighe("dataset.json");
		
		List<Pair> pairList = JsonUtil.retrieveCoupleList("listaRighe0", listaRighe);
		
		System.out.println(pairList.size());
		
//		for (Pair p : pairList) {
//			System.out.println(p.getFirstRiga().getId()+","+p.getSecondRiga().getId());
//		}

		System.out.println(pairList);
	}

}

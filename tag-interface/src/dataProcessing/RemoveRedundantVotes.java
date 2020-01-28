package dataProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datamodel.Voto;
import datamodel.VotoKey;
import utils.JsonUtil;

public class RemoveRedundantVotes {

	public static void main(String[] args) {

		List<Voto> listaVoti = JsonUtil.retrieveVoti();
		
		Map<VotoKey,Voto> mapVoti = new HashMap<>();
		
		for(Voto v : listaVoti) {
			VotoKey vk = new VotoKey();
			vk.setFirstId(v.getFirstId());
			vk.setSecondId(v.getSecondId());
			vk.setColumn(v.getColumn());
			mapVoti.put(vk, v);
		}
		
		List<Voto> updatedListaVoti = new ArrayList<>(mapVoti.values());
		
		JsonUtil.saveVotiOnFile(updatedListaVoti);
	}

}

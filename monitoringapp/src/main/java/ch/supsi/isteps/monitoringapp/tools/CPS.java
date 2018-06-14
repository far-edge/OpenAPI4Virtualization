package ch.supsi.isteps.monitoringapp.tools;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class CPS extends Element{
	
	public CPS() {
		super.setLayerName(LayerType.sensor.toString());
	}
	
	public CPS(String id, String newElementName) {
		super(id, newElementName);
		super.setLayerName(LayerType.sensor.toString());
	}
	
	public CPS(String id, String newElementName, String newLayerName, List<Pair<String, String>> newAttributes) {
		super(id, newElementName, newLayerName, newAttributes);
		super.setLayerName(newLayerName);
	}
}

package ch.supsi.isteps.monitoringapp.tools;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class SmartObject extends Element{
	
	public SmartObject() {
		super.setLayerName(LayerType.logical.toString());
	}
	
	public SmartObject(String id, String newElementName) {
		super(id, newElementName);
		super.setLayerName(LayerType.logical.toString());
	}
	
	public SmartObject(String id, String newElementName, String newLayerName, List<Pair<String, String>> newAttributes) {
		super(id, newElementName, newLayerName, newAttributes);
		super.setLayerName(newLayerName);
	}
}

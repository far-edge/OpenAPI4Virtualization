package ch.supsi.isteps.prototype;

import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;

public class TestDataModelProxy extends AbstractOpenAPIForVirtualization {

	@Override
	public String saveElement(String aLayer, String anArchetype, String anElementName) {
		return "";
	}

	@Override
	public void saveAttribute(String aLayerName, String anElementKey, String aKey, String aValue) {
	}

	@Override
	public String removeElementByName(String anElementName) {
		return "";
	}
}

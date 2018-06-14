package ch.supsi.isteps.prototype.tools;

public abstract class AbstractOpenAPIForVirtualization {

	public abstract String saveElement(String aLayerName, String anArchetype, String anElementName);
	public abstract void saveAttribute(String aLayerName, String anElementName, String aKey, String aValue);
	public abstract String removeElementByName(String anElementName) ;
}

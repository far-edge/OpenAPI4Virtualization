package ch.supsi.isteps.monitoringapp.tools;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractElement {
	
	public abstract String getId();
	
	protected abstract void setId(String id);
	
	public abstract String getElementName();
		
	protected abstract void setElementName(String name);
	
	public abstract String getLayerName();
	
	public abstract void setLayerName(String name);
	
	public abstract List<Pair<String, String>> getAttributes();
	
	public abstract void clearAttributes();
	
	public abstract void removeAttribute(Pair<String, String> toBeRemoved);
	
	public abstract void addAttribute(Pair<String, String> toBeAdded);
	
	public abstract String getElementType();
	
	public abstract void setElementType(String type);
	
	public abstract void fromFields(Fields fields); 
	
	public abstract String toString();

}

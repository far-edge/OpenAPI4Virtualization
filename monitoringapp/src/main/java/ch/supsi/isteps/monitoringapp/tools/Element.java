package ch.supsi.isteps.monitoringapp.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ch.supsi.isteps.monitoringapp.data.RealToDigitalSyncData;

public class Element extends AbstractElement {

	private String _id = "";
	private String _elementName = "";
	private String _layerName = "";
	private List<Pair<String, String>> _attributes = new ArrayList<>();
	private String ELEMENTIDKEY = "id";
	private String ELEMENTNAMEKEY = "elementName";
	private String ELEMENTLAYERNAMEKEY = "layerName";
	private String ELEMENTKEYSLISTKEY = "keys";
	private String ELEMENTARCHETYPEKEY = "archetype";

	private String _elementType = "";

	public Element() {
	}

	public Element(String id, String newElementName) {
		_id = id;
		_elementName = newElementName;
	}
	
	public Element(String id, String newElementName, String newLayerName, List<Pair<String, String>> newAttributes) {
		_id = id;
		_elementName = newElementName;
		_layerName = newLayerName;
		_attributes = newAttributes;
	}
	
	public String getId() {
		return _id;
	}

	protected void setId(String id) {
		_id = id;
	}

	public String getElementName() {
		return _elementName;
	}

	protected void setElementName(String name) {
		_elementName = name;
	}

	public String getLayerName() {
		return _layerName;
	}

	public void setLayerName(String name) {
		_layerName = name;
	}

	public List<Pair<String, String>> getAttributes() {
		return _attributes;
	}

	public void clearAttributes() {
		_attributes.clear();
	}

	public void removeAttribute(Pair<String, String> toBeRemoved) {
		_attributes.remove(toBeRemoved);
	}

	public void addAttribute(Pair<String, String> toBeAdded) {
		_attributes.add(toBeAdded);
	}
	
	public void addAttribute(String AttributeKey, String value) {
		
		_attributes.add(Pair.of(AttributeKey, value));
	}

	public String getElementType() {
		return _elementType;
	}

	public void setElementType(String type) {
		_elementType = type;
	}

	public void fromFields(Fields fields) {
		// search the cpsname
		String id = fields.firstValueFor(ELEMENTIDKEY);
		setId(id);
		// search the cpsname
		String name = fields.firstValueFor(ELEMENTNAMEKEY);
		setElementName(name);
		// search the layername
		String layerName = fields.firstValueFor(ELEMENTLAYERNAMEKEY);
		setLayerName(layerName);
		// search the elementtype
		String elementType = fields.firstValueFor(ELEMENTARCHETYPEKEY);
		setElementType(elementType);
		// search for the attributes
		String attributeKeys = fields.firstValueFor(ELEMENTKEYSLISTKEY);
		List<String> keys = new ArrayList<>();
		if (attributeKeys.length() > 0) {
			keys = Arrays.asList(attributeKeys.split(","));
			for (String key : keys) {
				Pair<String, String> pair = Pair.of(key, fields.firstValueFor(key));
				addAttribute(pair);
			}
		}

	}
	
	public String toString() {
		// building the fields string ->
		// elementName=EOLST|layerName=sensor|archetype=SMARTOBJECT|keys=key1,key2,key3,|key1=5|key2=value|key3=pippo|
		Fields fields = new Fields();
		fields.put("id", _id);
		fields.put("elementName", _elementName);
		fields.put("layerName", _layerName.toString());
		fields.put("archetype", _elementType.toString());

		// build list of keys for attributes in case there are attributes
		if (_attributes.size() > 0) {
			String attributesList = "";
			for (Pair<String, String> attribute : _attributes) {
				System.out.println("Attribute to be added to list " + attribute.getLeft());
				if(!attribute.getLeft().equals(RealToDigitalSyncData.ELEMENT_NAME)) {
					attributesList += attribute.getLeft() + ",";
				}
			}
			if (attributesList != null && attributesList.length() > 0 && attributesList.charAt(attributesList.length() - 1) == ',') {
				attributesList = attributesList.substring(0, attributesList.length() - 1);
		    }
			if(attributesList.length() > 0) {
				attributesList.substring(0, attributesList.length() - 1);
			}
			
			fields.put("keys", attributesList);
			for (Pair<String, String> attribute : _attributes) {
				if(!attribute.getLeft().equals(RealToDigitalSyncData.ELEMENT_NAME)) {
				fields.put(attribute.getLeft(), attribute.getRight());
				}
			}
		}
		System.out.println("From ELEMENT to FIELDS: " + fields.toRaw());
		return fields.toRaw();
	}
	
	public void parseAttributes(Fields fields) {
		// search for the attributes
		System.out.println("Parsing the attributes");
		List<String> keys = fields.keys();
		for (String key : keys) {
			addAttribute(Pair.of(key, fields.firstValueFor(key)));
		}
//		System.out.println("Filled attributes here");
//		System.out.println(getAttributes());
	}
	
	
}

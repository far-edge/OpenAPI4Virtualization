package ch.supsi.isteps.virtualfactory.openapi.persistence;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;
import ch.supsi.isteps.virtualfactory.tools.dispenser.AbstractDispenser;

public class InMemoryDataModelPersistence extends AbstractPersistence {

	private XMap<String> _layers;
	private XMap<Fields> _elements;
	private XMap<String> _elementsByName;
	private XMap<Fields> _attributes;
	private XMap<Fields> _assets;
	private XMap<Fields> _links;
	
	private AbstractDispenser _elementIdDispenser;
	private AbstractDispenser _assetsIdDispenser;

	public InMemoryDataModelPersistence(AbstractDispenser elementIdDispenser, AbstractDispenser assetsIdDispenser) {
		_layers = new XMap<String>("no layer");
		_elements = new XMap<Fields>(Fields.empty());
		_elementsByName = new XMap<String>("no element");
		_attributes = new XMap<Fields>(Fields.empty());
		_links = new XMap<Fields>(Fields.empty());
		_assets = new XMap<Fields>(Fields.empty());
		_elementIdDispenser = elementIdDispenser;
		_assetsIdDispenser = assetsIdDispenser;
	}

	// LAYER
	@Override
	public Fields saveLayer(Fields someFields) {
		_layers.put(someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME), someFields.firstValueFor(RealToDigitalSyncData.LAYER_DESCRIPTION));
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields retrieveLayerByName(Fields someFields) {
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		if (!_layers.containsKey(layerName)) return Fields.single(ToolData.OUTCOME, "true");
		String layerDescription = _layers.named(layerName);
		return Fields.single(ToolData.OUTCOME, "true").put(RealToDigitalSyncData.LAYER_NAME, layerName).put(RealToDigitalSyncData.LAYER_DESCRIPTION, layerDescription);
	}
	@Override
	public Fields removeLayerByName(Fields someFields) {
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		_layers.remove(layerName);
		return Fields.single(ToolData.OUTCOME, "true");
	}

	// ELEMENT
	@Override
	public Fields saveElement(Fields someFields) {
		String id = _elementIdDispenser.next();
		_elements.put(id, someFields);
		_elementsByName.put(someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME), id);
		return Fields.single(ToolData.OUTCOME, "true").put(RealToDigitalSyncData.ID, id);
	}
	@Override
	public Fields retrieveElementByName(Fields someFields) {
		System.out.println(_elementsByName.keys());
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		if (!_elementsByName.containsKey(elementName)) return Fields.single(ToolData.OUTCOME, "true");
		String id = _elementsByName.named(elementName);
		Fields result = _elements.named(id);
		return Fields.single(ToolData.OUTCOME, "true").put(RealToDigitalSyncData.ID, id).putAll(result);
	}
	@Override
	public Fields removeElementByName(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String id = _elementsByName.named(elementName);
		_elements.remove(id);
		_elementsByName.remove(elementName);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields retrieveAllElementsByLayer(Fields someFields) {
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		for (String each : _elements.keys()) {
			Fields currentElement = _elements.named(each);
			if (currentElement.firstValueFor(RealToDigitalSyncData.LAYER_NAME).equals(layerName)) {
				result.put("id", each);
				result.putAll(currentElement);
			}
		}
		return result;
	}
	@Override
	public Fields retrieveAllElementsByArchetype(Fields someFields) {
		String archetype = someFields.firstValueFor(RealToDigitalSyncData.ARCHETYPE);
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		for (String each : _elements.keys()) {
			Fields currentElement = _elements.named(each);
			if (currentElement.firstValueFor(RealToDigitalSyncData.ARCHETYPE).equals(archetype)) {
				result.put("id", each);
				result.putAll(currentElement);
			}
		}
		return result;
	}

	// ATTRIBUTES
	@Override
	public Fields saveAttribute(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		if (!_attributes.containsKey(elementName)) {
			_attributes.put(elementName, Fields.empty());
		}
		Fields attributes = _attributes.named(elementName);
		attributes.put(someFields.firstValueFor(RealToDigitalSyncData.KEY), someFields.firstValueFor(RealToDigitalSyncData.VALUE));
		_attributes.put(elementName, attributes);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields retrieveAttributeByKey(Fields someFields) {
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		for (String each : _attributes.keys()) {
			Fields currentFields = _attributes.named(each);
			if(currentFields.containsKey(key)) {
				result.put(RealToDigitalSyncData.ELEMENT_NAME, each);
				result.putAll(currentFields.select(key));
			}
		}
		return result;
	}
	@Override
	public Fields removeAttributeByKey(Fields someFields) {
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		for (String each : _attributes.keys()) {
			Fields currentFields = _attributes.named(each);
			for (String eachKey : currentFields.allKeysStartingWith(RealToDigitalSyncData.KEY)) {
				if(currentFields.firstValueFor(eachKey).equals(key)) {
					_attributes.put(each, currentFields.remove(eachKey));
				}
			}
		}
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields retrieveAllAttributesByElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String elementLayer = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		Fields attributes = _attributes.named(elementName);
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		result.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
		result.putAll(attributes);
		return result;
	}
	@Override
	public Fields removeAllAttributesByElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		_attributes.remove(elementName);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields removeAttributeOfElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		Fields attributes = _attributes.named(elementName);
		attributes.remove(key);
		_attributes.put(elementName, attributes);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	
	// LINK
	@Override
	public Fields saveLink(Fields someFields) {
		String linkFrom = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		if (!_links.containsKey(linkFrom)) {
			_links.put(linkFrom, Fields.empty());
		}
		Fields links = _links.named(linkFrom);
		links.put(RealToDigitalSyncData.TO_ELEMENT, someFields.firstValueFor(RealToDigitalSyncData.TO_ELEMENT));
		links.put(RealToDigitalSyncData.LINK_TYPE, someFields.firstValueFor(RealToDigitalSyncData.LINK_TYPE));
		_links.put(linkFrom, links);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	@Override
	public Fields retrieveLinkBySourceElement(Fields someFields) {
		String fromElement = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		Fields links = _links.named(fromElement);
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		result.put(RealToDigitalSyncData.FROM_ELEMENT, fromElement);
		result.putAll(links);
		return result;
	}
	@Override
	public Fields retrieveLinkById(Fields someFields) {
		String id = someFields.firstValueFor(RealToDigitalSyncData.ID);
		Fields links = _links.named(id);
		Fields result = Fields.single(ToolData.OUTCOME, "true");
		result.put(RealToDigitalSyncData.ID, id);
		result.putAll(links);
		return result;
	}
	@Override
	public Fields removeLinkBySourceElement(Fields someFields) {
		String fromElement = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		_links.remove(fromElement);
		return Fields.single(ToolData.OUTCOME, "true");
	}
	
	// UTILS
	@Override
	public void clear() {
		_elements = new XMap<Fields>(Fields.empty());
		_attributes = new XMap<Fields>(Fields.empty());
	}

	@Override
	public Fields saveAsset(Fields someFields) {
		String id = _assetsIdDispenser.next();
		_assets.put(id, someFields);
		return Fields.single("outcome", "true").put("id", id);
	}

	@Override
	public Fields retrieveAllAssets() {
		Fields result = Fields.empty();
		for (String eachKey : _assets.keys()) {
			result.putAll(_assets.named(eachKey).select(RealToDigitalSyncData.ASSET_FILE_NAME,
					RealToDigitalSyncData.ASSET_FILE_PATH));
		}
		return result;
	}

	@Override
	public Fields retrieveAssetByFileName(Fields someFields) {
		for (String each : _assets.keys()) {
			Fields currentFields = _assets.named(each);
			if (currentFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME).contains(someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME))) {
				return currentFields;
			}
		}
		return Fields.empty();
	}

	@Override
	public void removeAssetByFileName(Fields someFields) {
		for (String each : _assets.keys()) {
			Fields currentFields = _assets.named(each);
			if (currentFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME).contains(someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME))) {
				_assets.remove(each);
			}
		}
	}
}
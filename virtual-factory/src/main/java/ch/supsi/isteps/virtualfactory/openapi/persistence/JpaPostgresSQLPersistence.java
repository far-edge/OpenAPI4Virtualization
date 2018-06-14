package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.util.List;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class JpaPostgresSQLPersistence extends AbstractPersistence {

	private ElementRepository _elementRepository;
	private AttributeRepository _attributeRepository;
	private AssetRepository _assetRepository;
	private LayerRepository _layerRepository;
	private LinkRepository _linkRepository;

	public JpaPostgresSQLPersistence(ElementRepository elementRepository, AttributeRepository attributeRepository,
			AssetRepository assetRepository, LayerRepository layerRepository, LinkRepository linkRepository) {
		_elementRepository = elementRepository;
		_attributeRepository = attributeRepository;
		_assetRepository = assetRepository;
		_layerRepository = layerRepository;
		_linkRepository = linkRepository;
	}

	// LAYER
	@Override
	public Fields saveLayer(Fields someFields) {
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		String layerDescription = someFields.firstValueFor(RealToDigitalSyncData.LAYER_DESCRIPTION);
		System.out.println("SAVE LAYER " + layerName + " --- " + someFields.toRaw());
		_layerRepository.save(new Layer(layerName, layerDescription));
		return Fields.single("outcome", "true");
	}

	@Override
	public Fields retrieveLayerByName(Fields someFields) {
		// select * from layers where layerName = layerName
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		System.out.println("RETRIEVE LAYER BY NAME " + layerName + " --- " + someFields.toRaw());
		Layer layer = _layerRepository.retrieveLayerByName(layerName);
		if(layer != null)
			return Fields.single("outcome", "true").putAll(layer.toFields());
		else
			return Fields.single("outcome", "true");
	}

	@Override
	public Fields removeLayerByName(Fields someFields) {
		// delete from layers where layerName = layerName
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		System.out.println("REMOVE LAYER " + layerName + " --- " + someFields.toRaw());
		_layerRepository.removeLayerByName(layerName);
		return Fields.single("outcome", "true");

	}

	// ELEMENT
	@Override
	public Fields saveElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		System.out.println("SAVE ELEMENT " + elementName + " --- " + someFields.toRaw());
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		String archetypeName = someFields.firstValueFor(RealToDigitalSyncData.ARCHETYPE);
		Element element = _elementRepository.save(new Element(layerName, elementName, archetypeName));
		return Fields.single("outcome", "true").putAll(element.toFields());
	}

	@Override
	public Fields retrieveElementByName(Fields someFields) {
		// SELECT * FROM elements WHERE name = "elementName";
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		System.out.println("RETRIEVE ELEMENT BY NAME " + elementName + " --- " + someFields.toRaw());
		List<Element> elements = _elementRepository.retrieveElementByName(elementName);
		Fields result = Fields.empty();
		// "outcome=true|id=125|layerName=LogicalLayer|archetype=Conveyor|elementName=Conveyor1"
		for (Element element : elements) {
			result.putAll(element.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);
	}

	@Override
	public Fields removeElementByName(Fields someFields) {
		// DELETE FROM elements WHERE name = "elementName";
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		System.out.println("REMOVE ELEMENT " + someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME) + " --- "
				+ someFields.toRaw());
		_elementRepository.removeElementByName(elementName);
		return Fields.single("outcome", "true");

	}

	@Override
	public Fields retrieveAllElementsByLayer(Fields someFields) {
		// SELECT * FROM elements WHERE layerName = "layer";
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		System.out.println("RETRIEVE ALL ELEMENTS BY LAYER " + layerName + " --- " + someFields.toRaw());
		List<Element> elements = _elementRepository.retrieveAllElementsByLayer(layerName);
		Fields result = Fields.empty();
		// "outcome=true|id=125|layerName=LogicalLayer|archetype=Conveyor|elementName=Conveyor1"
		for (Element element : elements) {
			result.putAll(element.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);

	}

	@Override
	public Fields retrieveAllElementsByArchetype(Fields someFields) {
		String archetypeName = someFields.firstValueFor(RealToDigitalSyncData.ARCHETYPE);
		System.out.println("RETRIEVE ALL ELEMENTS BY ARCHETYPE " + archetypeName + " --- " + someFields.toRaw());
		List<Element> elements = _elementRepository.retrieveAllElementsByArchetype(archetypeName);
		Fields result = Fields.empty();
		// "outcome=true|id=125|layerName=LogicalLayer|archetype=Conveyor|elementName=Conveyor1"
		for (Element element : elements) {
			result.putAll(element.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);

	}

	// ATTRIBUTES
	@Override
	public Fields saveAttribute(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		
		String elementId = retrieveElementId(layerName, elementName);
		System.out.println("SAVE ATTRIBUTE " + elementName + " --- " + someFields.toRaw());
		_attributeRepository.save(new Attribute(elementId, elementName, someFields.firstValueFor(RealToDigitalSyncData.KEY),someFields.firstValueFor(RealToDigitalSyncData.VALUE)));
		return Fields.single(ToolData.OUTCOME, "true").putAll(someFields);
	}

	// is this really needed in case of multiple elements?
	@Override
	public Fields retrieveAttributeByKey(Fields someFields) {
		// select * from attributes where key = "key";
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		System.out.println("RETRIEVE ATTRIBUTE BY KEY" + key + " --- " + someFields.toRaw());
		List<Attribute> attributes = _attributeRepository.retrieveAttributeByKey(key);
		Fields result = Fields.empty();
		for (Attribute attribute : attributes) {
			result.putAll(attribute.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);
	}

	// is this really needed in case of multiple elements?
	@Override
	public Fields removeAttributeByKey(Fields someFields) {
		// DELETE FROM attributes WHERE key = "key";
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		System.out.println("REMOVE ATTRIBUTE BY KEY" + key + " --- " + someFields.toRaw());
		_attributeRepository.removeAttributeByKey(key);
		return Fields.single(ToolData.OUTCOME, "true");

	}

	@Override
	public Fields retrieveAllAttributesByElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String layerName = someFields.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		
		String elementId = retrieveElementId(layerName, elementName);
		
		System.out.println("RETRIEVE ATTRIBUTES BY ELEMENT --- " + elementName);
		List<Attribute> attributes = _attributeRepository.findByElementId(elementId);
		//List<Attribute> attributes = _attributeRepository.findByElementName(layerName, elementName);
		Fields result = Fields.empty();
		result.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
		for (Attribute each : attributes) {
			System.out.println(each.toFields());
			result.putAll(each.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);

	}

	@Override
	public Fields removeAllAttributesByElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		System.out.println("REMOVE ALL ATTRIBUTES OF ELEMENT --- " + elementName + " --- " + someFields.toRaw());
		_attributeRepository.removeAllAttributesByElement(elementName);
		return Fields.single(ToolData.OUTCOME, "true");

	}

	@Override
	public Fields removeAttributeOfElement(Fields someFields) {
		String elementName = someFields.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME);
		String key = someFields.firstValueFor(RealToDigitalSyncData.KEY);
		System.out.println("REMOVE ATTRIBUTE OF ELEMENT --- " + elementName + " --- " + someFields.toRaw());
		_attributeRepository.removeAttributeOfElement(key, elementName);
		return Fields.single(ToolData.OUTCOME, "true");

	}

	// LINK
	@Override
	public Fields saveLink(Fields someFields) {
		String fromElement = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		String toElement = someFields.firstValueFor(RealToDigitalSyncData.TO_ELEMENT);
		String linkType = someFields.firstValueFor(RealToDigitalSyncData.LINK_TYPE);
		System.out.println("SAVE LINK FROM " + fromElement + " TO " + toElement + " --- " + someFields.toRaw());
		Link link = _linkRepository.save(new Link(fromElement, toElement, linkType));
		return Fields.single(ToolData.OUTCOME, "true").putAll(link.toFields());
	}

	@Override
	public Fields retrieveLinkBySourceElement(Fields someFields) {
		String fromElement = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		System.out.println("RETRIEVE LINK FROM " + fromElement + " --- " + someFields.toRaw());
		Fields result = Fields.empty();
		List<Link> links = _linkRepository.retrieveLinkBySourceElement(fromElement);
		for (Link link : links) {
			result.putAll(link.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);

	}

	@Override
	public Fields removeLinkBySourceElement(Fields someFields) {
		String fromElement = someFields.firstValueFor(RealToDigitalSyncData.FROM_ELEMENT);
		System.out.println("REMOVE LINK FROM " + fromElement + " --- " + someFields.toRaw());
		_linkRepository.removeLinkBySourceElement(fromElement);
		return Fields.single(ToolData.OUTCOME, "true");

	}
	
	@Override
	public Fields retrieveLinkById(Fields someFields) {
		String id = someFields.firstValueFor(RealToDigitalSyncData.ID);
		System.out.println("RETRIEVE LINK BY ID " + id + " --- " + someFields.toRaw());
		Fields result = Fields.empty();
		List<Link> links = _linkRepository.retrieveLinkById(Long.parseLong(id));
		for (Link link : links) {
			result.putAll(link.toFields());
		}
		return Fields.single(ToolData.OUTCOME, "true").putAll(result);

	}

	// UTILS
	@Override
	public void clear() {
		_elementRepository.deleteAll();
		_attributeRepository.deleteAll();
	}

	@Override
	public Fields saveAsset(Fields someFields) {
		String assetFileName = someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME);
		System.out.println("SAVE ASSET " + assetFileName + " --- " + someFields.toRaw());

		Asset currentAsset = _assetRepository
				.save(new Asset(someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_PATH) + "/" + assetFileName));
		System.out.println(">>>>>" + currentAsset.toString());
		Fields result = Fields.single("outcome", "true");
		currentAsset.copyIdIn(result);
		return result;
	}

	@Override
	public Fields retrieveAllAssets() {
		System.out.println("RETRIEVE ALL ASSETS");
		Iterable<Asset> assets = _assetRepository.findAll();
		Fields result = Fields.empty();
		for (Asset asset : assets) {
			result.putAll(asset.toFields());
		}
		return Fields.single("outcome", "true").putAll(result);
	}

	@Override
	public Fields retrieveAssetByFileName(Fields someFields) {
		// select * from assets a where a.filePath contains assetFileName;
		String assetFileName = someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME);
		System.out.println("RETRIEVE ASSET BY FILENAME " + assetFileName + " --- " + someFields.toRaw());
		Asset asset = _assetRepository.retrieveAssetByFileName(assetFileName);
		return Fields.single("outcome", "true").putAll(asset.toFields());
	}

	@Override
	public void removeAssetByFileName(Fields someFields) {
		// delete from assets a where a.filePath contains assetFileName;
		String assetFileName = someFields.firstValueFor(RealToDigitalSyncData.ASSET_FILE_NAME);
		System.out.println("REMOVE ASSET BY FILENAME " + assetFileName + " --- " + someFields.toRaw());
		_assetRepository.removeAssetByFileName(assetFileName);
	}
	
	private String retrieveElementId(String layerName, String elementName) {
		List<Element> elements = _elementRepository.retrieveElementByNameAndLayer(elementName, layerName);
		System.out.println("retrieveElementId -> found " + elements.size() + "elements");
		String elementId = "";
		if(elements.size()>0)
		{
			elementId = elements.get(0).id();
		}
		return elementId;
	}
}

package ch.supsi.isteps.virtualfactory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.DataModelSystemFactory;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AssetRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AttributeRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.ElementRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.InMemoryDataModelPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.JpaPostgresSQLPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LayerRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LinkRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.SingletonPersisistence;
import ch.supsi.isteps.virtualfactory.openapi.responseformatter.AsFieldsResponseFormatter;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.APILevel0Mapper;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel0;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel1;
import ch.supsi.isteps.virtualfactory.tools.dispenser.UUIDDispenser;

@RestController
@Profile("layer0")
public class Layer0Controller {

	@Autowired
	private ElementRepository _elementRepository;
	@Autowired
	private AttributeRepository _attributeRepository;
	@Autowired
	private AssetRepository _assetRepository;
	@Autowired
	private LayerRepository _layerRepository;
	@Autowired
	private LinkRepository _linkRepository;

	private APILevel0Mapper _mapper;
	
	
	public Layer0Controller(@Value("${virtualfactory.persistence-selection}") String persistenceSelection,ElementRepository elementRepository, AttributeRepository attributeRepository,
			AssetRepository assetRepository, LayerRepository layerRepository, LinkRepository linkRepository) {
		_elementRepository = elementRepository;
		_attributeRepository = attributeRepository;
		_assetRepository = assetRepository;
		_layerRepository = layerRepository;
		_linkRepository = linkRepository;
		
		AbstractPersistence persistence = SingletonPersisistence.iniciate(persistenceSelection, _elementRepository, _attributeRepository, _assetRepository, _layerRepository, _linkRepository);
		_mapper = new APILevel0Mapper();
		
	}

	// BEGIN OF THE OPEN API FOR VIRTUALIZATION INTERFACE

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.POST)
	public String createElement(@RequestBody MultiValueMap<String, String> params) {
		// TODO to change the strings to RealToDigitalSynchData constants
		String elementName = "";
		String layerName = "";
		String archetype = "";

		if (params.get("anElementName").size() > 0) {
			elementName = params.get("anElementName").get(0);
		}
		if (params.get("aLayerName").size() > 0) {
			layerName = params.get("aLayerName").get(0);
		}
		if (params.get("anArchetype").size() > 0) {
			archetype = params.get("anArchetype").get(0);
		}
		return _mapper.saveElement(layerName, archetype, elementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.GET)
	@ResponseBody
	public String readElement(@PathVariable String anElementName) {
		return _mapper.retrieveElementByName(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteElementByName(@PathVariable String anElementName) {
		return _mapper.removeElementByName(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveAllElementsByLayer(@PathVariable String aLayerName) {
		return _mapper.retrieveAllElementsByLayer(aLayerName);
	}

	@RequestMapping(value = "/archetype/{anArchetype}", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveAllElementsByArchetype(@PathVariable String anArchetype) {
		return _mapper.retrieveAllElementsByArchetype(anArchetype);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}/attribute", method = RequestMethod.POST)
	@ResponseBody
	public void addAttributeForElement(@PathVariable String aLayerName, @PathVariable String anElementName,
			@RequestBody MultiValueMap<String, String> params) {
		String key = "";
		if (params.get(RealToDigitalSyncData.KEY).size() > 0) {
			key = params.get(RealToDigitalSyncData.KEY).get(0);
		}
		String value = "";
		if (params.get(RealToDigitalSyncData.VALUE).size() > 0) {
			value = params.get(RealToDigitalSyncData.VALUE).get(0);
		}
		System.out.println("Attribute value: " + value);
		_mapper.saveAttribute(aLayerName, anElementName, key, value);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}/attribute", method = RequestMethod.GET)
	@ResponseBody
	public String getElementAttributes(@PathVariable String aLayerName, @PathVariable String anElementName) {
		return _mapper.retrieveAttributesByElement(aLayerName, anElementName);
	}

	@RequestMapping(value = "layer/sensor/element/{anElementName}/attribute/{anAttribute}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeElementAttribute(@PathVariable String anElementName, @PathVariable String anAttribute,
			@RequestBody MultiValueMap<String, String> params) {
		String key = "";
		if (params.get(RealToDigitalSyncData.KEY).size() > 0) {
			key = params.get(RealToDigitalSyncData.KEY).get(0);
		}
		String value = "";
		if (params.get(RealToDigitalSyncData.VALUE).size() > 0) {
			value = params.get(RealToDigitalSyncData.VALUE).get(0);
		}
		_mapper.removeAttribute(anElementName, key, value);
	}

	@RequestMapping(value = "/assets/{filePath}", method = RequestMethod.DELETE)
	public void removeAsset(@PathVariable String filePath) {
		_mapper.removeAsset(filePath);
	}

	@RequestMapping(value = "/assets", method = RequestMethod.POST)
	public String createAsset(@RequestBody String filePath) {
		return _mapper.saveAsset(filePath);
	}

	@RequestMapping("/assets")
	@ResponseBody
	public String retrieveAllAssets() {
		return _mapper.retrieveAllAssets();
	}

	@RequestMapping("/assets/{filePath}/details")
	@ResponseBody
	public String assetDetails(@PathVariable String filePath) {
		return _mapper.retrieveAssetDetails(filePath);
	}

}

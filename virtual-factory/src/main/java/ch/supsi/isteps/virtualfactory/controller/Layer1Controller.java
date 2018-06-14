package ch.supsi.isteps.virtualfactory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
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

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AssetRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AttributeRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.ElementRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LayerRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LinkRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.SingletonPersisistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel1;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;

@RestController
@Profile("layer1")
public class Layer1Controller {

	private OpenAPIForVirtualizationLevel1 _openAPIForVirtualizationLevel1AsFields;

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

	
	public Layer1Controller(@Value("${virtualfactory.persistence-selection}") String persistenceSelection, ElementRepository elementRepository, AttributeRepository attributeRepository,
			AssetRepository assetRepository, LayerRepository layerRepository, LinkRepository linkRepository) {
		
		_elementRepository = elementRepository;
		_attributeRepository = attributeRepository;
		_assetRepository = assetRepository;
		_layerRepository = layerRepository;
		_linkRepository = linkRepository;
		
		AbstractPersistence persistence = SingletonPersisistence.iniciate(persistenceSelection, _elementRepository, _attributeRepository, _assetRepository, _layerRepository, _linkRepository);
		
		_openAPIForVirtualizationLevel1AsFields = new OpenAPIForVirtualizationLevel1();

	}

	// BEGIN OF THE OPEN API FOR VIRTUALIZATION INTERFACE
	@RequestMapping(value = "/connection-status", method = RequestMethod.GET)
	@ResponseBody
	public String connectionStatus() {
		return _openAPIForVirtualizationLevel1AsFields.connectionStatus();
	}

	// TODO: rethink methods is GET right?
	@RequestMapping(value = "/connect-platform", method = RequestMethod.GET)
	@ResponseBody
	public Boolean connect() {
		return _openAPIForVirtualizationLevel1AsFields.connect();
	}

	// TODO: rethink methods is GET right?
	@RequestMapping(value = "/disconnect-platform", method = RequestMethod.GET)
	@ResponseBody
	public Boolean disconnect() {
		return _openAPIForVirtualizationLevel1AsFields.disconnect();
	}

	@RequestMapping("/setup")
	@ResponseBody
	public Boolean setup() {
		return _openAPIForVirtualizationLevel1AsFields.setup();
	}

	@RequestMapping("/clear-all")
	@ResponseBody
	public Boolean clearAll() {
		return _openAPIForVirtualizationLevel1AsFields.clearAll();
	}

	@RequestMapping(value = "/active-sensors", method = RequestMethod.GET)
	@ResponseBody
	public String activeSensors() {
		String s = _openAPIForVirtualizationLevel1AsFields.activeSensors();
		return s;
	}

	@RequestMapping(value = "/sensor-data/{anArchetype}", method = RequestMethod.GET)
	@ResponseBody
	public String sensorData(@PathVariable String anArchetype) {
		String s = _openAPIForVirtualizationLevel1AsFields.sensorData(anArchetype);
		return s;
	}

	@RequestMapping("layer/{aLayerName}/element/{anElementName}/attribute")
	@ResponseBody
	public String retrieveAttributesByElement(@PathVariable String anElementName, @PathVariable String aLayerName) {
		return _openAPIForVirtualizationLevel1AsFields.retrieveAttributesByElement(aLayerName, anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.POST)
	public String saveElement(@RequestBody MultiValueMap<String, String> params) {
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
		return _openAPIForVirtualizationLevel1AsFields.saveElement(layerName, archetype, elementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.GET)
	@ResponseBody
	public String getElement(@PathVariable String anElementName) {
		return _openAPIForVirtualizationLevel1AsFields.getElement(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.GET)
	@ResponseBody
	public String layerElements(@PathVariable String aLayerName) {
		return _openAPIForVirtualizationLevel1AsFields.layerElements(aLayerName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.PUT, consumes = "text/plain", headers = "Accept=text/plain")
	@ResponseBody
	public String updateElement(@PathVariable String anElementName, @RequestBody String params) {
		return _openAPIForVirtualizationLevel1AsFields.updateElement(anElementName, params);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.DELETE)
	public String removeElement(@PathVariable String anElementName) {
		return _openAPIForVirtualizationLevel1AsFields.removeElement(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/attribute/paired", method = RequestMethod.GET)
	@ResponseBody
	public String getPairedElements() {
		return _openAPIForVirtualizationLevel1AsFields.getPairedElements();
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}/attribute", method = RequestMethod.POST)
	@ResponseBody
	public String addAttributeForElement(@PathVariable String aLayerName, @PathVariable String anElementName, @RequestBody MultiValueMap<String, String> params) {
		String key = "";
		if (params.get(RealToDigitalSyncData.KEY).size() > 0) {
			key = params.get(RealToDigitalSyncData.KEY).get(0);
		}
		String value = "";
		if (params.get(RealToDigitalSyncData.VALUE).size() > 0) {
			value = params.get(RealToDigitalSyncData.VALUE).get(0);
		}
		System.out.println("Attribute value: " + value);
		return _openAPIForVirtualizationLevel1AsFields.addAttributeForElement(aLayerName, anElementName, key, value);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/attribute/pairings", method = RequestMethod.GET)
	@ResponseBody
	public String getPairings(@PathVariable String aLayerName) {
		return _openAPIForVirtualizationLevel1AsFields.getPairings(aLayerName);
	}

	@RequestMapping(value = "layer/{aLayerName}/element/{anElementName}/attribute/pairing", method = RequestMethod.DELETE)
	@ResponseBody
	public void removePairing(@PathVariable String anElementName,@PathVariable String aLayerName) {
		_openAPIForVirtualizationLevel1AsFields.removePairing(aLayerName, anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/attribute/active", method = RequestMethod.GET)
	@ResponseBody
	public String getActiveElements() {
		return _openAPIForVirtualizationLevel1AsFields.getActiveElements();
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}/attribute", method = RequestMethod.GET)
	@ResponseBody
	public String getElementAttributes(@PathVariable String aLayerName, @PathVariable String anElementName) {
		return _openAPIForVirtualizationLevel1AsFields.getElementAttributes(aLayerName, anElementName);
	}

	@RequestMapping(value = "/assets", method = RequestMethod.POST)
	public String createAsset(@RequestBody String filePath) {
		return _openAPIForVirtualizationLevel1AsFields.createAsset(filePath);
	}

	@RequestMapping("/assets")
	@ResponseBody
	public String retrieveAllAssets() {
		return _openAPIForVirtualizationLevel1AsFields.retrieveAllAssets();
	}

	@RequestMapping("/assets/{filePath}/details")
	@ResponseBody
	public String assetDetails(@PathVariable String filePath) {
		return _openAPIForVirtualizationLevel1AsFields.assetDetails(filePath);
	}

	@RequestMapping(value = "/assets/{filePath}", method = RequestMethod.DELETE)
	public void removeAsset(@PathVariable String filePath) {
		_openAPIForVirtualizationLevel1AsFields.removeAsset(filePath);
	}

	private List<Pair<String, String>> getAttributes(Fields fieldsList) {
		List<Pair<String, String>> keysList = new ArrayList<>();
		String attributeKeys = fieldsList.firstValueFor("keys");
		List<String> keys = new ArrayList<>();
		if (attributeKeys.length() > 0) {
			keys = Arrays.asList(attributeKeys.split(","));
			for (String key : keys) {
				keysList.add(Pair.of(key, fieldsList.firstValueFor(key)));
			}
		}
		return keysList;
	}

}
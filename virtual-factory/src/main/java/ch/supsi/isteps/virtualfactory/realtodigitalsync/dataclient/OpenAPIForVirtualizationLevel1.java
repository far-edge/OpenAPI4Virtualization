package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.PathVariable;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.ConfiguratorSystemFactory;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.PlatFormSystemFactory;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.FaredgePlatformCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class OpenAPIForVirtualizationLevel1 {

	private TestFaredgeKafkaClient _faredgeApiClient;
	private XSystem _platformSystem;
	private APILevel0Mapper _mapper;
	

	public OpenAPIForVirtualizationLevel1() {
		_faredgeApiClient = new TestFaredgeKafkaClient();
		_mapper = new APILevel0Mapper();
		
		XSystem faredgeRealToDigitalPlatform = FaredgeApiSystemFactory.create(_faredgeApiClient,
				new ProductiveConnectionFactory(_mapper.getAPILevel0()));
		XSystem configurator = ConfiguratorSystemFactory.create(_mapper.getAPILevel0());
		_platformSystem = PlatFormSystemFactory.create(faredgeRealToDigitalPlatform, configurator);
	}
	
	public String connectionStatus() {
		Fields connectionPlatformStatusInput = Fields.single(ToolData.COMMAND_NAME,
				FaredgePlatformCommandData.CONNECTION_STATUS);
		Fields connectionPlatformStatusOutput = Fields.empty();
		_platformSystem.execute(connectionPlatformStatusInput, connectionPlatformStatusOutput);
		return connectionPlatformStatusOutput.select("status").toRaw();
	}
	
	public Boolean connect() {
		Fields connectionPlatformInput = Fields.single(ToolData.COMMAND_NAME,
				FaredgePlatformCommandData.CONNECT_PLATFORM);
		Fields connectionPlatformOutput = Fields.empty();
		_platformSystem.execute(connectionPlatformInput, connectionPlatformOutput);
		return true;
	}
	
	
	public Boolean disconnect() {
		Fields disconnectionPlatformInput = Fields.single(ToolData.COMMAND_NAME,
				FaredgePlatformCommandData.DISCONNECT_PLATFORM);
		Fields disconnectionPlatformOutput = Fields.empty();
		_platformSystem.execute(disconnectionPlatformInput, disconnectionPlatformOutput);
		return true;
	}
	
	public Boolean setup() {
		System.out.println("SETTING UP THE MOCK DATA");
		_faredgeApiClient.clear();
		_faredgeApiClient.addSensor("BOL");
		_faredgeApiClient.addSensor("EOLGR");
		_faredgeApiClient.addSensor("EOLST");
		_faredgeApiClient.addSensorMessage("BOL", SenMLFaredgeMessages.whr_BOL("G1", "1276020076"));
		_faredgeApiClient.addSensorMessage("BOL", SenMLFaredgeMessages.whr_BOL("E7", "1276020077"));
		_faredgeApiClient.addSensorMessage("EOLGR", SenMLFaredgeMessages.whr_EOL_GR("G1", "1276020076"));
		_faredgeApiClient.addSensorMessage("EOLGR", SenMLFaredgeMessages.whr_EOL_GR("E7", "1276020077"));
		_faredgeApiClient.addSensorMessage("EOLST", SenMLFaredgeMessages.whr_EOL_ST("G1", "1276020076"));
		_faredgeApiClient.addSensorMessage("EOLST", SenMLFaredgeMessages.whr_EOL_ST("E7", "1276020077"));
		return true;
	}

	
	public Boolean clearAll() {
		_mapper.clearAll();
		return true;
	}
	
	public String activeSensors() {
		//This method requests all active elements and extracts the related archetypes
//		List<String> activeSensors = _faredgeApiClient.retrieveActiveSensors();
//		Fields result = Fields.empty();
//		for (String each : activeSensors) {
//			result.put(RealToDigitalSyncData.ACTIVE_SENSORS, each);
//		}
		//return result.toRaw();
		Fields result = Fields.empty();
		Fields activeSensors = Fields.fromRaw(getActiveElements());
		List<String> activeArchetypes = new ArrayList();
		for (String each : activeSensors.keys()) {
			String activeElementName = activeSensors.firstValueFor(each);
			Fields activeElement = Fields.fromRaw(getElement(activeElementName));
			activeArchetypes.add(activeElement.firstValueFor(RealToDigitalSyncData.ARCHETYPE));
		}
		List<String> activeArchetypesWithoutDuplicates = new ArrayList<>(new HashSet<>(activeArchetypes));
	    for (String archetype : activeArchetypesWithoutDuplicates) {
	    	result.put(RealToDigitalSyncData.ACTIVE_SENSORS, archetype);
		}
		return result.toRaw();
	}
	
	public String sensorData(String anArchetype) {
		String result = _mapper.retrieveAllElementsByArchetype(anArchetype);
		Fields fields = Fields.fromRaw(result);
		//cleaning of the result from the elements different from the ones of the "sensor" layer.
		//if the layer type is different from sensor (readings) remove it from the list
		for (String keyName : fields.allKeysStartingWith(RealToDigitalSyncData.LAYER_NAME)) {
			if(!fields.firstValueFor(keyName).equals("sensor")){
				String index = keyName.replace(RealToDigitalSyncData.LAYER_NAME, "");
				for (String key : fields.keys()) {
					if(key.contains(index)){
						fields.remove(key);
					}
				}
			}
		}
		return fields.toRaw();
	}

	public String retrieveAttributesByElement(String aLayerName, String anElementName) {
		return _mapper.retrieveAttributesByElement(aLayerName, anElementName);
	}
	public String saveElement(String layerName, String archetype, String elementName) {
		return _mapper.saveElement(layerName, archetype, elementName);
	}
	
	public String getElement(String anElementName) {
		return _mapper.retrieveElementByName(anElementName);
	}
	
	public String layerElements(String aLayerName) {
		String result = "";
		switch (aLayerName) {
		case ConstantData.SENSOR:
			result = _mapper.retrieveAllElementsByLayer(ConstantData.SENSOR);
			break;
		case ConstantData.LOGICAL:
			result = _mapper.retrieveAllElementsByLayer(ConstantData.LOGICAL);
			break;
		default:
			result = _mapper.retrieveAllElementsByLayer(aLayerName);
		}
		return result;
	}
	
	public String updateElement(String anElementName, String params) {
		// update the element fields and for every attribute update it if necessary
		Fields fieldsList = Fields.fromRaw(params);

		String aLayerName = "";
		String anArchetype = "";

		if (fieldsList.containsKey(RealToDigitalSyncData.LAYER_NAME)) {
			aLayerName = fieldsList.firstValueFor(RealToDigitalSyncData.LAYER_NAME);
		}
		if (fieldsList.containsKey(RealToDigitalSyncData.ARCHETYPE)) {
			anArchetype = fieldsList.firstValueFor(RealToDigitalSyncData.ARCHETYPE);
		}
		//get the old attributes before removing the element
		List<Pair<String, String>> oldAttributes = new ArrayList<Pair<String, String>>();
		Fields fieldsOldAttributes = Fields.fromRaw(retrieveAttributesByElement(aLayerName, anElementName));
		
		//start updating
		_mapper.removeElementByName(anElementName);
		_mapper.saveElement(aLayerName, anArchetype, anElementName);

		// retrieve all the attributes
		List<Pair<String, String>> newAttributes = getAttributes(fieldsList);
		//remove old attributes	
		for (String key : fieldsOldAttributes.keys()) {
			if (!key.equals(RealToDigitalSyncData.ELEMENT_NAME)) {
				oldAttributes.add(Pair.of(key, fieldsOldAttributes.firstValueFor(key)));
			}
		}
		for (Pair<String, String> pair : oldAttributes) {
			_mapper.removeAttribute(anElementName, pair.getLeft(), pair.getRight());
		}
		// insert new attributes
		for (Pair<String, String> pair : newAttributes) {
			_mapper.saveAttribute(aLayerName, anElementName, pair.getLeft(), pair.getRight());
		}
		System.out.println("Attributes addded "	+ _mapper.retrieveAttributesByElement(aLayerName, anElementName));
		return _mapper.retrieveElementByName(anElementName);
	}
	

	public String removeElement(String anElementName) {
		Fields element = Fields.fromRaw(_mapper.retrieveElementByName(anElementName));
		Fields attributes = Fields.fromRaw(_mapper.retrieveAttributesByElement(element.firstValueFor(RealToDigitalSyncData.LAYER_NAME), anElementName));

		for (Map.Entry<String, String> attribute : Fields.toMap(attributes).entrySet()) {
			_mapper.removeAttribute(anElementName, attribute.getKey(), attribute.getValue());
		}
		return _mapper.removeElementByName(anElementName);

	}
	
	public String getPairedElements() {
		String output = retrieveElementsByAttribute(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.PAIRED);
		output += retrieveElementsByAttribute(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.ACTIVE);
		return output;
	}

	public String addAttributeForElement(String aLayerName, String anElementName, String key, String value) {
		_mapper.saveAttribute(aLayerName, anElementName, key, value);
		return _mapper.retrieveAttributesByElement(aLayerName, anElementName);
	}

	public String getPairings(String aLayerName) {
		Fields result = new Fields();
		String output = retrieveElementsByAttribute(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.PAIRED);
		output += retrieveElementsByAttribute(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.ACTIVE);

		Fields fields = Fields.fromRaw(output);
		Fields elements = fields.selectPrefix(RealToDigitalSyncData.ELEMENT_NAME);
		for (Map.Entry<String, String> entry : Fields.toMap(elements).entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue());
			Fields attributes = Fields.fromRaw(_mapper.retrieveAttributesByElement(aLayerName, entry.getValue()));
			String jar = attributes.firstValueFor(RealToDigitalSyncData.PAIRING);
			result.put(RealToDigitalSyncData.ELEMENT_NAME, entry.getValue() + " - " + jar);
		}
		return result.toRaw();
	}

	public void removePairing(String aLayerName, String anElementName) {
		Fields fields = Fields.fromRaw(_mapper.retrieveAttributesByElement(aLayerName, anElementName));
		String jarName = fields.firstValueFor(RealToDigitalSyncData.PAIRING);
		_mapper.removeAttribute(anElementName, RealToDigitalSyncData.PAIRING, jarName);
		_mapper.removeAttribute(anElementName, RealToDigitalSyncData.STATUS,
				RealToDigitalSyncData.PAIRED);
	}

	public String getActiveElements() {
		String output = retrieveElementsByAttribute(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.ACTIVE);
		return output;
	}

	public String getElementAttributes(String layerName, String anElementName) {
		return _mapper.retrieveAttributesByElement(layerName, anElementName);
	}
	
	public void saveLayer(String layerName, String layerDescription) {
		_mapper.saveLayer(layerName, layerDescription);
	}
	
	public void saveLink(String fromElement, String toElement, String linkType) {
		_mapper.saveLink(fromElement, toElement, linkType);
	}
	
	public String retrieveLinkBySourceElement(String fromElement) {
		return _mapper.retrieveLinkBySourceElement(fromElement);
	}
	
	public String retrieveLinkById(String id) {
		return _mapper.retrieveLinkById(id);
	}

	public String createAsset(String filePath) {
		return _mapper.saveAsset(filePath);
	}

	public String retrieveAllAssets() {
		return _mapper.retrieveAllAssets();
	}

	public String assetDetails(String filePath) {
		return _mapper.retrieveAssetDetails(filePath);
	}

	public void removeAsset(String filePath) {
		_mapper.removeAsset(filePath);
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
	
	private String retrieveElementsByAttribute(String key, String value) {
		Fields result = Fields.empty();
		Fields fields = Fields.fromRaw(_mapper.retrieveAllElementsByLayer(ConstantData.LOGICAL));

		Fields elements = fields.selectPrefix(RealToDigitalSyncData.ELEMENT_NAME);
		for (Map.Entry<String, String> entry : Fields.toMap(elements).entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue());
			Fields attributes = Fields.fromRaw(_mapper.retrieveAttributesByElement(ConstantData.LOGICAL, entry.getValue()));
			for (Map.Entry<String, String> attribute : Fields.toMap(attributes).entrySet()) {
				if (attribute.getKey().equals(key) && attribute.getValue().equals(value)) {
					System.out.println("Added to results");
					result.put(RealToDigitalSyncData.ELEMENT_NAME, entry.getValue());
				}
			}
		}
		String output = "";
		for (Map.Entry<String, String> entry : Fields.toMap(result).entrySet()) {
			System.out.println(entry.getKey() + "|" + entry.getValue());
			output += entry.getKey() + "=" + entry.getValue() + "|";

		}
		return output;
	}
}

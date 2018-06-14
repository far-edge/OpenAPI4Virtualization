package ch.supsi.isteps.monitoringapp.faredgeplatform.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ch.supsi.isteps.monitoringapp.authentication.Authentication;
import ch.supsi.isteps.monitoringapp.data.ConfigurationData;
import ch.supsi.isteps.monitoringapp.data.RealToDigitalSyncData;
import ch.supsi.isteps.monitoringapp.tools.AbstractElement;
import ch.supsi.isteps.monitoringapp.tools.CPS;
import ch.supsi.isteps.monitoringapp.tools.ElementType;
import ch.supsi.isteps.monitoringapp.tools.Fields;
import ch.supsi.isteps.monitoringapp.tools.LayerType;
import ch.supsi.isteps.monitoringapp.tools.SmartObject;

class FaredgePlatformFacade extends AbstractPlatformFacade {

	private String _baseUrl;
	private RestTemplate _restTemplate;
	private List<String> activeCPS = new ArrayList<String>();
	private List<String> existingCPS = new ArrayList<String>();

	public FaredgePlatformFacade(String baseUrl) {
		_baseUrl = baseUrl;
		_restTemplate = new RestTemplate();
	}

	// BEGIN OF THE PLATFORM MANAGEMENT SYSTEM

	// returns the status of the platform connection
	@Override
	public boolean isConnected() {
		System.out.println("CONNECTION STATUS REQUESTED");
		Fields result = Fields.fromRaw(_restTemplate.getForObject(_baseUrl + "connection-status", String.class));
		return result.firstValueFor(RealToDigitalSyncData.STATUS).equals("connected") ? true : false;
	}

	// connects the platform
	@Override
	public void disconnect() {
		System.out.println("DISCONNECT");
		_restTemplate.getForObject(_baseUrl + "disconnect-platform", String.class);
	}

	// disconnects the platform
	@Override
	public void connect() {
		System.out.println("CONNECT");
		_restTemplate.getForObject(_baseUrl + "connect-platform", String.class);
	}

	// setup of the backend system creating fake elements and values for the
	// simulation
	@Override
	public void setup() {
		String path = _baseUrl + "setup";
		System.out.println("SETUP " + path);
		_restTemplate.getForObject(path, String.class);
	}

	// cleaning of all the inserted data in the datamodel from the setup function
	@Override
	public void clearAll() {
		System.out.println("CLEAR ALL");
		_restTemplate.getForObject(_baseUrl + "clear-all", String.class);
	}

	// comparison of the inserted credentials with the ones configured, these can be
	// extended and additional login platfom can be used
	@Override
	public boolean compareCredentials(String username, String password) {
		System.out.println("COMPARE CREDENTIALS");
		Authentication auth = new Authentication();
		return auth.authenticate(username, password);
	}

	// retrieves all the sensor elements configured in the datamodel
	@Override
	public String retrieveSensorElements() {
		String path = _baseUrl + "active-sensors";
		System.out.println("RETRIEVE ARCHITYPES OF ACTIVE ELEMENTS " + path);
		try {
			return _restTemplate.getForObject(path, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// retrieve all the sensor elemens given the archetype
	@Override
	public String retrieveSensorDataElements(String anArchetype) {
		try {
			String path = _baseUrl + "sensor-data/" + anArchetype;
			System.out.println("RETRIEVE SENSOR DATA ELEMENT " + path);
			String result = _restTemplate.getForObject(path, String.class);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// retrieve all attributes given the element name - sensorId
	@Override
	public String retrieveAttributesByElement(String sensorId) {
		LayerType layer = LayerType.sensor;
		String path = _baseUrl + "layer/" + layer + "/element/" + sensorId + "/attribute";
		System.out.println("RETRIEVE ATTRIBUTES OF ELEMENT " + path);
		try {
			return _restTemplate.getForObject(path, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// END OF THE PLATFORM MANAGEMENT SYSTEM
	// BEGIN OF THE OPEN API SYSTEM

	// insert a new CPS in the list of the existing ones
	@Override
	public void createSensor(String newSensor) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("anElementName", newSensor);
		params.add("aLayerName", LayerType.sensor.toString());
		params.add("anArchetype", ElementType.BOL.toString());

		// String response = _restTemplate.postForObject(_baseUrl + "element", params,
		// String.class);
		System.out.println("SAVING ELEMENT " + newSensor + " "
				+ String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.sensor.toString()));
		String response = _restTemplate.postForObject(
				String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.sensor.toString()), params, String.class);

		try {
			if (response == null || response.isEmpty()) {
				throw new Exception("Error in saving the element with name: " + newSensor);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT SAVED SENSOR " + result.toRaw());
	}

	// get sensor element and all attribute details
	@Override
	public CPS getSensor(String sensorName) {
		try {
			// get element
			String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.sensor.toString(),
					sensorName);

			// String path = _baseUrl + "element/" + aSensorName;
			System.out.println("RETRIEVE SENSOR DATA ELEMENT " + path);
			String result = _restTemplate.getForObject(path, String.class);

			if (result != null) {
				// to be converted from string to CPS
				Fields fromRaw = Fields.fromRaw(result);
				CPS cps = new CPS();
				cps.fromFields(fromRaw);
				System.out.println("to be converted from string to CPS");

				// get attributes
				path = _baseUrl + "layer/" + LayerType.sensor.toString() + "/element/" + sensorName + "/attribute";
				System.out.println("RETRIEVE SENSOR ATTRIBUTES " + path);
				result = _restTemplate.getForObject(path, String.class);
				System.out.println(result);
				// to be converted from string to CPS
				cps.parseAttributes(Fields.fromRaw(result));

				return cps;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("RETRIEVE SENSOR DATA ELEMENT RETURNED NO RESULTS");
		return null;
	}

	// get the list of the existing sensor elements
	@Override
	public List<String> getExistingSensors() {
		List<String> list = new ArrayList<>();
		String path = String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.sensor.toString());
		System.out.println("GET SENSOR ELEMENTS " + path);
		String response = _restTemplate.getForObject(path, String.class);
		if (response == null || response.isEmpty())
			return list;
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT EXISTING SENSORS " + result.toRaw());
		// get only names of the elements
		for (String keyName : result.allKeysStartingWith(RealToDigitalSyncData.ELEMENT_NAME)) {
			list.add(result.firstValueFor(keyName, null));
		}
		return list;
	}

	// update of a sensor element - element is recreated and the attributes are
	// recreated
	@Override
	public void updateSensor(AbstractElement sensor) {
		String fields = sensor.toString();

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add(RealToDigitalSyncData.FIELDS, fields);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Accept", MediaType.TEXT_PLAIN.toString());
		headers.add("Content-Type", MediaType.TEXT_PLAIN.toString());
		HttpEntity<String> entity = new HttpEntity<>(fields, headers);

		// This method sends a CPS in order to update the element

		System.out.println("Built to fields: " + fields);
		try {
			String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.sensor.toString(),
					sensor.getElementName());

			System.out.println("UPDATE ELEMENT " + path);
			ResponseEntity<String> response = _restTemplate.exchange(path, HttpMethod.PUT, entity, String.class,
					fields);
			System.out.println("SENSOR ELEMENT HAS BEEN UPDATED " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// deletion of an existing element
	@Override
	public void deleteSensor(String toBeRemovedSensor) {
		String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.sensor.toString(),
				toBeRemovedSensor);
		System.out.println("REMOVE ELEMENT " + path);
		_restTemplate.delete(path);
		System.out.println("RESULT REMOVED SENSOR");
	}

	// // deletion of an existing element
	// @Override
	// public void removeExistingCPS(String anElementName) {
	// String response = _restTemplate.postForObject(_baseUrl + "removeElement",
	// anElementName, String.class);
	// System.out.println("Response from server " + response);
	// try {
	// if (response == null || response.isEmpty()) {
	// throw new Exception("Error in removing the element with name: " +
	// anElementName);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.out.println(response);
	// Fields result = Fields.fromRaw(response);
	// System.out.println("RESULT REMOVED SENSOR " + result.toRaw());
	// }

	// insertion of a new smart object element in the list of the exsisting smart
	// object elements
	@Override
	public void createSmartObject(String newSmartObject) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("anElementName", newSmartObject);
		params.add("aLayerName", LayerType.logical.toString());
		params.add("anArchetype", ElementType.COMPLEXELEMENT.toString());

		// String response = _restTemplate.postForObject(_baseUrl + "element", params,
		// String.class);
		System.out.println("SAVING ELEMENT " + newSmartObject + " "
				+ String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.logical.toString()));
		String response = _restTemplate.postForObject(
				String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.logical.toString()), params, String.class);

		try {
			if (response == null || response.isEmpty()) {
				throw new Exception("Error in saving the element with name: " + newSmartObject);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT SAVED SMARTOBJECT " + result.toRaw());
	}

	// retrieve smart object element and all attribute details
	@Override
	public SmartObject getSmartObject(String smartObjectName) {

		try {
			// get element
			String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.logical.toString(),
					smartObjectName);

			System.out.println("RETRIEVE SMART OBJECT ELEMENT " + path);
			String result = _restTemplate.getForObject(path, String.class);

			if (result != null) {
				// to be converted from string to SmartObject
				Fields fromRaw = Fields.fromRaw(result);
				SmartObject so = new SmartObject();
				so.fromFields(fromRaw);
				System.out.println("to be converted from string to Smart Object");
				System.out.println(so.getElementType());

				// get attributes
				path = _baseUrl + "layer/" + LayerType.logical.toString() + "/element/" + smartObjectName
						+ "/attribute";
				System.out.println("RETRIEVE SMARTOBJECT ATTRIBUTES " + path);
				result = _restTemplate.getForObject(path, String.class);
				System.out.println(result);
				// to be converted from string to SMARTOBJECT
				so.parseAttributes(Fields.fromRaw(result));
				return so;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("RETRIEVE SMARTOBJECT ELEMENT RETURNED NO RESULTS");
		return null;

		// try {
		// String path = _baseUrl + "element/" + smartObjectName;
		// System.out.println("RETRIEVE SMART OBJECT ELEMENT " + path);
		// String result = _restTemplate.getForObject(path, String.class);
		// if (result != null) {
		// // to be converted from string to CPS
		// Fields fromRaw = Fields.fromRaw(result);
		// SmartObject so = new SmartObject();
		// so.fromFields(fromRaw);
		// System.out.println("to be converted from string to SmartObject");
		// return so;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println("RETRIEVE LOGICAL ELEMENT RETURNED NO RESULTS");
		// return null;
	}

	// retrieve the list of the exising smart object elements configured in the
	// system
	@Override
	public List<String> getSmartObjects() {
		List<String> list = new ArrayList<>();
		String path = String.format("%1$slayer/%2$s/element", _baseUrl, LayerType.logical.toString());
		System.out.println("GET SMART OBJECT ELEMENTS " + path);
		String response = _restTemplate.getForObject(path, String.class);
		if (response == null || response.isEmpty())
			return list;
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT EXISTING SMART OBJECTS " + result.toRaw());
		// get only names of the elements
		for (String keyName : result.allKeysStartingWith(RealToDigitalSyncData.ELEMENT_NAME)) {
			list.add(result.firstValueFor(keyName, null));
		}
		return list;

	}

	// update of a smart object element - element is recreated with the same name
	// (name cannot be changed) and the attributes
	// are recreated still linked to the same element identified by the name
	@Override
	public void updateSmartObject(AbstractElement smartObject) {
		System.out.println("UPDATE SMART OBJECT ELEMENT " + smartObject.getElementName());
		String fields = smartObject.toString();

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add(RealToDigitalSyncData.FIELDS, fields);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Accept", MediaType.TEXT_PLAIN.toString());
		headers.add("Content-Type", MediaType.TEXT_PLAIN.toString());
		HttpEntity<String> entity = new HttpEntity<>(fields, headers);

		// This method sends a smart object in order to update the element

		System.out.println("Built to fields: " + fields);
		try {
			String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.logical.toString(),
					smartObject.getElementName());

			System.out.println(path);
			ResponseEntity<String> response = _restTemplate.exchange(path, HttpMethod.PUT, entity, String.class,
					fields);
			System.out.println("SMART OBJECT HAS BEEN UPDATED " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// remove the existing smart object element
	@Override
	public void deleteSmartObject(String toBeRemovedSmartObject) {

		String path = String.format("%1$slayer/%2$s/element/%3$s", _baseUrl, LayerType.logical.toString(),
				toBeRemovedSmartObject);
		System.out.println("REMOVE ELEMENT " + toBeRemovedSmartObject + " " + path);
		_restTemplate.delete(path);
		System.out.println("RESULT REMOVED SMART OBJECT");
	}

	// get the list of paired sensors
	@Override
	public List<String> getPairedSensors() {
		List<String> list = new ArrayList<>();
		String path = _baseUrl + "layer/" + LayerType.logical + "/element/attribute/paired";
		System.out.println("GET PAIRED ELEMENTS " + path);
		String response = _restTemplate.getForObject(path, String.class);
		if (response == null || response.isEmpty())
			return list;
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT PAIRED SENSORS " + result.toRaw());
		// get only names of the elements
		for (String keyName : result.allKeysStartingWith("elementName")) {
			list.add(result.firstValueFor(keyName, null));
		}
		return list;
	}

	// mark the sensor as paired and add the jarname as attribute
	@Override
	public void savePairedSensor(String newPairedSensor, String jarName) {
		// mark as paired
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add(RealToDigitalSyncData.KEY, RealToDigitalSyncData.STATUS);
		params.add(RealToDigitalSyncData.VALUE, RealToDigitalSyncData.PAIRED);

		String path = _baseUrl + "layer/" + LayerType.logical + "/element/" + newPairedSensor + "/attribute";
		System.out.println("INSERT NEW PAIRING " + path);
		String response = _restTemplate.postForObject(path, params, String.class);
		try {
			if (response == null || response.isEmpty()) {
				throw new Exception("Error in pairing the element with name: " + newPairedSensor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// add pairing jar
		MultiValueMap<String, String> paramsJar = new LinkedMultiValueMap<String, String>();
		paramsJar.add(RealToDigitalSyncData.KEY, RealToDigitalSyncData.PAIRING);
		paramsJar.add(RealToDigitalSyncData.VALUE, jarName);

		path = _baseUrl + "layer/" + LayerType.logical + "/element/" + newPairedSensor + "/attribute";
		String responseJar = _restTemplate.postForObject(path, paramsJar, String.class);
		try {
			if (responseJar == null || responseJar.isEmpty()) {
				throw new Exception("Error in pairing the element with name: " + newPairedSensor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Fields result = Fields.fromRaw(responseJar);
		System.out.println("RESULT PAIRED CPS " + result.toRaw());
	}

	// deletion of the pairing of a sensor element - not needed since the pairing is
	// managed through the update function
	@Override
	public void deletePairedSensor(String toBeRemovedSensor) {
	}

	// get the list of paired sensors with the related jars
	@Override
	public List<String> getPairings() {
		List<String> list = new ArrayList<>();
		String path = _baseUrl + "layer/" + LayerType.logical + "/element/attribute/pairings";
		System.out.println("GET PAIRINGS " + path);
		String response = _restTemplate.getForObject(path, String.class);
		System.out.println("Response from server: " + response);

		if (response == null || response.isEmpty())
			return list;
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT PAIRED SENSORS " + result.toRaw());
		// get only names of the elements
		for (String keyName : result.allKeysStartingWith("elementName")) {
			list.add(result.firstValueFor(keyName, null));
		}
		return list;
	}

	// ad a pairing sensor - jar -- this is not used: refer to insertNewInPairedCPS
	@Override
	public void savePairing(String newPairing) {

	}

	// remove pairing sensor - jar -- this is not used: refer to removePairedCPS
	@Override
	public void removePairing(String pairingToBeRemoved) {
		String path = _baseUrl + "layer/" + LayerType.logical + "/element/" + pairingToBeRemoved + "/attribute/pairing";
		System.out.println("REMOVE PAIRING " + path);
		_restTemplate.delete(path);
		System.out.println("PAIRING REMOVED FROM ELEMENT " + pairingToBeRemoved);
	}

	// get the list of acrive sensors
	@Override
	public List<String> getActiveSensors() {
		List<String> list = new ArrayList<>();
		String path = _baseUrl + "layer/" + LayerType.logical + "/element/attribute/active";
		System.out.println("GET ACTIVE SENSORS " + path);
		String response = _restTemplate.getForObject(path, String.class);
		if (response == null || response.isEmpty())
			return list;
		Fields result = Fields.fromRaw(response);
		System.out.println("RESULT ACTIVE SENSORS " + result.toRaw());
		// get only names of the elements
		for (String keyName : result.allKeysStartingWith(RealToDigitalSyncData.ELEMENT_NAME)) {
			System.out.println("added " + keyName + " " + result.firstValueFor(keyName, null));
			list.add(result.firstValueFor(keyName, null));
		}
		return list;
	}

	// There are 3 categories of elements (existing-paired-active) and this function
	// updates the elements paired -> active and active -> paired
	@Override
	public void saveActiveSensor(List<String> activeSensors) {
		System.out.println("selectedSensorsArchetypes");
		System.out.println(activeSensors);
		System.out.println("activeCPS");
		System.out.println(activeCPS = getActiveSensors());
		System.out.println("existingCPS");
		System.out.println(existingCPS = getSmartObjects());
		for (String element : existingCPS) {
			if (activeCPS.contains(element) && (!activeSensors.contains(element))) {
				// changing status from active to paired
				System.out.println("Changing status from active to paired");
				activeCPS.remove(element);
				// call server to remove CPS
				SmartObject so = getSmartObject(element);
				so.removeAttribute(Pair.of(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.ACTIVE));
				so.addAttribute(Pair.of(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.PAIRED));
				System.out.println("Saving element");
				updateSmartObject(so);
				System.out.println("Element saved");
			} else if (!activeCPS.contains(element) && (activeSensors.contains(element))) {
				// changing status from paired to active
				System.out.println("Changing status from paired to active");
				activeCPS.add(element);
				// call server to add CPS
				SmartObject so = getSmartObject(element);
				so.removeAttribute(Pair.of(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.PAIRED));
				so.addAttribute(Pair.of(RealToDigitalSyncData.STATUS, RealToDigitalSyncData.ACTIVE));
				System.out.println("Saving element");
				updateSmartObject(so);
				System.out.println("Element saved");
			}
		}
		System.out.println(activeCPS.size());
		System.out.println(activeCPS);

		// System.out.println("selectedSensorsArchetypes");
		// System.out.println(activeSensors);
		// System.out.println("activeCPS");
		// System.out.println(activeCPS = getActiveSensors());
		// System.out.println("existingCPS");
		// System.out.println(existingCPS = getExistingSensors());
		// for (String cps : existingCPS) {
		// if (activeCPS.contains(cps) && (!activeSensors.contains(cps))) {
		// // changing status from active to paired
		// System.out.println("Changing status from active to paired");
		// activeCPS.remove(cps);
		// // call server to remove CPS
		// CPS cpsObject = getSensor(cps);
		// cpsObject.removeAttribute(Pair.of(RealToDigitalSyncData.STATUS,
		// RealToDigitalSyncData.ACTIVE));
		// cpsObject.addAttribute(Pair.of(RealToDigitalSyncData.STATUS,
		// RealToDigitalSyncData.PAIRED));
		// System.out.println("Saving element");
		// updateSensor(cpsObject);
		// System.out.println("Element saved");
		// } else if (!activeCPS.contains(cps) && (activeSensors.contains(cps))) {
		// // changing status from paired to active
		// System.out.println("Changing status from paired to active");
		// activeCPS.add(cps);
		// // call server to add CPS
		// CPS cpsObject = getSensor(cps);
		// cpsObject.removeAttribute(Pair.of(RealToDigitalSyncData.STATUS,
		// RealToDigitalSyncData.PAIRED));
		// cpsObject.addAttribute(Pair.of(RealToDigitalSyncData.STATUS,
		// RealToDigitalSyncData.ACTIVE));
		// System.out.println("Saving element");
		// updateSensor(cpsObject);
		// System.out.println("Element saved");
		// }
		// }
		// System.out.println(activeCPS.size());
		// System.out.println(activeCPS);

	}

	// upload of the configuration file to the file server
	@Override
	public void uploadBAOS(ByteArrayOutputStream os, String aFilename) {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("user-file", getUserFileResource(os, aFilename));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
		ResponseEntity<String> response = _restTemplate.exchange(ConfigurationData.FILE_SERVER_URL + "upload",
				HttpMethod.POST, requestEntity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		if (statusCode.is2xxSuccessful()) {
			String fileUrl = response.getBody();
			_restTemplate.postForObject(_baseUrl + "assets", fileUrl, String.class);
		}
	}

	// retrieve all assets uploaded previously in the system
	@Override
	public List<String> getJars() {
		String path = _baseUrl + "assets";
		System.out.println("GET INSTALLED JARS " + path);
		String requestResult = _restTemplate.getForObject(path, String.class);
		ArrayList<String> result = new ArrayList<String>();
		// result.add("TestToBeRemovedFromFacade");
		if (requestResult == null || requestResult.isEmpty()) {
			return result;
		}
		Fields response = Fields.fromRaw(requestResult);
		for (String each : response.allKeysStartingWith("assetFileName")) {
			result.add(response.firstValueFor(each));
		}
		return result;
	}

	// get the details of a previously uploaded jar configuration
	@Override
	public ArrayList<Pair<String, String>> getJarDetails(String aFileName) {
		String path = _baseUrl + "assets/" + aFileName + "/details";
		System.out.println("GET JAR DETAILS " + path);
		Fields result = Fields.fromRaw(_restTemplate.getForObject(path, String.class));
		ArrayList<Pair<String, String>> details = new ArrayList<Pair<String, String>>();
		for (String eachKey : result.allKeysStartingWith("category")) {
			String categoryName = result.firstValueFor(eachKey);
			details.add(Pair.of(eachKey, categoryName));
			details.add(Pair.of(categoryName, result.firstValueFor(categoryName)));
		}
		return details;
	}

	@Override
	public FileResource downloadConfigurationTemplate() {
		// String path =
		// "http://fileserver:7003/sources/git/faredge/virtual-factory/src/main/resources/download/prototype.zip";
		// System.out.println(path);
		// FileResource fileResource = new FileResource(new File(path));
		// return fileResource;

		System.out.println("Accesing resource in " + ConfigurationData.FILESERVERTEMPLATEPATH);
		String path = ConfigurationData.FILESERVERTEMPLATEPATH;

		try {
			// call the endpoint to retrieve the file
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> response = _restTemplate.exchange(path, HttpMethod.GET, entity, byte[].class);
			// save the file
			Files.write(Paths.get("prototype.zip"), response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileResource fileResource = new FileResource(new File("prototype.zip"));
		return fileResource;

		// StreamResource stream = null;
		// try {
		// //call the endpoint to retrieve the file
		// HttpHeaders headers = new HttpHeaders();
		// headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		// HttpEntity<String> entity = new HttpEntity<>(headers);
		// ResponseEntity<byte[]> response = _restTemplate.exchange(path,
		// HttpMethod.GET, entity, byte[].class);
		//
		// //parse the response to return the file
		// stream = new StreamResource(new StreamSource() {
		// private static final long serialVersionUID = 5320263964861009068L;
		// @Override
		// public InputStream getStream() {
		// try {
		// return new ByteArrayInputStream(response.getBody());
		// } catch (Exception e) {
		// return null;
		// }
		// }
		// }, "prototype.zip");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return stream;

	}

	// remove a previously uploaded jar configuration
	@Override
	public void removeLoadedJAR(String aFileName) {
		_restTemplate.delete(_baseUrl + "assets/" + aFileName);
		// TODO remove also on the fileServer!
	}

	// retrieve of the file previously uploaded to the file server given the name
	private FileSystemResource getUserFileResource(ByteArrayOutputStream os, String aFileName) {
		try {
			File temp = File.createTempFile(aFileName.replace(".jar", ""), "");
			FileOutputStream fileOutputStream = new FileOutputStream(temp);
			os.writeTo(fileOutputStream);
			fileOutputStream.close();
			os.close();
			return new FileSystemResource(temp);
		} catch (Exception e) {
			throw new RuntimeException("The temp file cannot be created for upload. " + aFileName);
		}
	}

}

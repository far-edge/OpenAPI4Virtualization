package ch.supsi.isteps.monitoringapp.faredgeplatform.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

import ch.supsi.isteps.monitoringapp.tools.AbstractElement;
import ch.supsi.isteps.monitoringapp.tools.CPS;
import ch.supsi.isteps.monitoringapp.tools.Fields;
import ch.supsi.isteps.monitoringapp.tools.SmartObject;

class FakePlatformFacade extends AbstractPlatformFacade {

	private String FILEPATH = "tmp/";
	// private String ELEMENTNAMEKEY = "elementName";
	private String ELEMENTLAYERNAMEKEY = "layerName";
	private String ELEMENTKEYSLISTKEY = "keys";
	private String ELEMENTIDKEY = "id";

	private Boolean isConnected = true;

	String[] arrayexistingCPS = { "BOL", "EOLGR", "EOLST" };
	List<String> listexistingCPS = Arrays.asList(arrayexistingCPS);
	private List<String> existingCPS = new ArrayList<String>(listexistingCPS);
	String[] arrayPairedCPS = { "BOL", "EOLST" };
	List<String> listPairedCPS = Arrays.asList(arrayPairedCPS);
	private List<String> pairedCPS = new ArrayList<String>(listPairedCPS);
	String[] arrayactiveCPS = { "BOL" };
	List<String> listactiveCPS = Arrays.asList(arrayactiveCPS);
	private List<String> activeCPS = new ArrayList<String>(listactiveCPS);
	String[] arraypairings = { "BOL - senml.jar", "EOLST - xml.jar" };
	List<String> listpairings = Arrays.asList(arraypairings);
	private List<String> pairings = new ArrayList<String>(listpairings);
	String[] arrayloadedJars = { "senml.jar", "xml.jar", "json.jar" };
	List<String> listloadedJars = Arrays.asList(arrayloadedJars);
	private List<String> loadedJars = new ArrayList<String>(listloadedJars);
	String[] arrayExistingSmartObject = { "SO1", "SO2", "SO3" };
	List<String> listExistingSmartObject = Arrays.asList(arrayExistingSmartObject);
	private List<String> existingSmartObject = new ArrayList<String>(listExistingSmartObject);

	private String username = "admin";
	private String password = "admin";

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void disconnect() {
		isConnected = false;
	}

	@Override
	public void connect() {
		isConnected = true;
	}

	@Override
	public void setup() {
	}

	@Override
	public void clearAll() {
	}

	@Override
	public boolean compareCredentials(String username, String password) {
		if ((username.equals("") && password.equals(""))
				|| (username.equals(this.username) && password.equals(this.password))) {
			return true;
		}
		return false;
	}

	@Override
	public String retrieveSensorElements() {
		Fields result = Fields.empty();
		result.put("activeSensors", "BOL");
		result.put("activeSensors", "EOLST");
		result.put("activeSensors", "EOLGR");
		return result.toRaw();
	}

	@Override
	public String retrieveSensorDataElements(String anArchetype) {
		Fields result = Fields.empty();
		result.put("elements", "BOL-1");
		result.put("elements", "BOL-2");
		result.put("elements", "EOLST-1");
		result.put("elements", "EOLST-2");
		result.put("elements", "EOLGR-1");
		result.put("elements", "EOLGR-2");
		return result.toRaw();
	}

	@Override
	public String retrieveAttributesByElement(String sensorId) {
		Fields result = Fields.empty();
		result.put("productionLine", "E1");
		result.put("timestamp", "123");
		return result.toRaw();
	}

	@Override
	public void createSensor(String newSensor) {
		System.out.println("Received element to be saved: " + newSensor);
		existingCPS.add(newSensor);
	}

	@Override
	public CPS getSensor(String sensorName) {
		String cpsString = "elementName=" + sensorName
				+ "|layerName=sensor|keys=key1,key2,key3|key1=5|key2=value|key3=pippo";
		System.out.println(cpsString);
		Fields f = Fields.fromRaw(cpsString);
		CPS cps = new CPS();
		cps.fromFields(f);
		return cps;
	}

	@Override
	public List<String> getExistingSensors() {
		return existingCPS;
	}

	@Override
	public void updateSensor(AbstractElement sensor) {
		String toBeSend = "elementName=" + sensor.getElementName() + "|layerName=" + sensor.getLayerName() + "|keys=";
		for (Pair<String, String> attribute : sensor.getAttributes()) {
			toBeSend += attribute.getLeft() + ",";
		}
		toBeSend += "|";
		for (Pair<String, String> attribute : sensor.getAttributes()) {
			toBeSend += attribute.getLeft() + "=" + attribute.getRight() + "|";
		}
		System.out.println("cps string to be send " + toBeSend);
	}

	@Override
	public void deleteSensor(String toBeRemovedSensor) {
		existingCPS.remove(toBeRemovedSensor);
	}
	
	
	
	
	@Override
	public void createSmartObject(String newSmartObject) {
		existingSmartObject.add(newSmartObject);

	}
	
	@Override
	public SmartObject getSmartObject(String smartObjectName) {
		String soString = "id=279b42ae-c23c-4da2-9127-49bde6b17173|elementName=" + smartObjectName
				+ "|layerName=sensor|keys=key1,key2,key3|key1=5|key2=value|key3=pippo";
		System.out.println(soString);

		Fields f = Fields.fromRaw(soString);

		// Search for the Id
		String id = f.firstValueFor(ELEMENTIDKEY);
		// search the cpsname
		// String name = f.firstValueFor(ELEMENTNAMEKEY);

		// create element
		SmartObject so = new SmartObject(id, smartObjectName);

		// search the layername
		String layerName = f.firstValueFor(ELEMENTLAYERNAMEKEY);
		so.setLayerName(layerName);
		// search for the attributes
		String attributeKeys = f.firstValueFor(ELEMENTKEYSLISTKEY);
		List<String> keys = new ArrayList<>();
		keys = Arrays.asList(attributeKeys.split(","));
		for (String key : keys) {
			Pair<String, String> sss = Pair.of(key, f.firstValueFor(key));
			so.addAttribute(sss);
		}
		return so;
	}
	
	@Override
	public List<String> getSmartObjects() {
		return existingSmartObject;
	}

	@Override
	public void updateSmartObject(AbstractElement smartObject) {
		String toBeSend = "elementName=" + smartObject.getElementName() + "|layerName=" + smartObject.getLayerName()
				+ "|keys=";
		for (Pair<String, String> attribute : smartObject.getAttributes()) {
			toBeSend += attribute.getLeft() + ",";
		}
		toBeSend += "|";
		for (Pair<String, String> attribute : smartObject.getAttributes()) {
			toBeSend += attribute.getLeft() + "=" + attribute.getRight() + "|";
		}
		System.out.println("smartobject string to be send " + toBeSend);
	}

	@Override
	public void deleteSmartObject(String toBeRemovedSmartObject) {
		existingSmartObject.remove(toBeRemovedSmartObject);

	}

	@Override
	public List<String> getPairedSensors() {
		return pairedCPS;
	}

	@Override
	public void savePairedSensor(String newPairedSensor, String jarName) {
		pairedCPS.add(newPairedSensor);
	}

	@Override
	public void deletePairedSensor(String toBeRemovedSensor) {
		pairedCPS.remove(toBeRemovedSensor);
	}

	
	@Override
	public List<String> getPairings() {
		return pairings;
	}

	@Override
	public void savePairing(String newPairing) {
		System.out.println("Received pairing to be saved " + newPairing);
		pairings.add(newPairing);
	}

	@Override
	public void removePairing(String pairingToBeRemoved) {
		System.out.println("Received pairing to be removed " + pairingToBeRemoved);
		pairings.remove(pairingToBeRemoved);

	}

	@Override
	public List<String> getActiveSensors() {
		return activeCPS;
	}

	@Override
	public void saveActiveSensor(List<String> activeSensors) {
		System.out.println(activeSensors.size());

		System.out.println(activeSensors);
		System.out.println(activeCPS);

		for (String cps : existingCPS) {
			if (activeCPS.contains(cps) && (!activeSensors.contains(cps))) {
				activeCPS.remove(cps);
			} else if (!activeCPS.contains(cps) && (activeSensors.contains(cps))) {
				activeCPS.add(cps);
			}
		}
		System.out.println(activeCPS.size());
		System.out.println(activeCPS);
	}
	
	
	
	
	
	
	

	@Override
	public void uploadBAOS(ByteArrayOutputStream os, String filename) {
		try {
			System.out.println("Saving the file to: " + FILEPATH + filename);
			FileOutputStream fos = new FileOutputStream(new File(FILEPATH + filename));
			fos.write(os.toByteArray());
			fos.flush();
			fos.close();

			loadedJars.add(filename);

		} catch (IOException ioe) {
			// Handle exception here
			ioe.printStackTrace();
		}

	}
	
	

	@Override
	public List<String> getJars() {
		return loadedJars;
	}

	@Override
	public FileResource downloadConfigurationTemplate() {
		
//		StreamSource ss = new StreamSource() {
//            public InputStream getStream() {
//                try {
//                    return new FileInputStream(new File("tmp/file.pdf"));
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        };
//        return new StreamResource(ss, "file.pdf");
		
		
		
		
		
		FileResource fileResource = new FileResource(new File("tmp/file.pdf"));
		return fileResource;
	}



	
	@Override
	public ArrayList<Pair<String, String>> getJarDetails(String configurationName) {
		ArrayList<Pair<String, String>> details = new ArrayList<Pair<String, String>>();
		switch (configurationName) {
		case "json.jar":
			details.add(Pair.of("category1", "Connection type"));
			details.add(Pair.of("category2", "Sintattic validation"));
			details.add(Pair.of("category3", "Semantic validation"));
			details.add(Pair.of("category4", "Message interpretation"));
			details.add(Pair.of("category5", "Datamodel mapping"));

			details.add(Pair.of("Connection type", "Kafka"));
			details.add(Pair.of("Sintattic validation", "One+One"));
			details.add(Pair.of("Semantic validation", "Active"));
			details.add(Pair.of("Message interpretation", "Json"));
			details.add(Pair.of("Datamodel mapping", "NoSql database"));
			break;
		case "xml.jar":
			details.add(Pair.of("category1", "Connection type"));
			details.add(Pair.of("category2", "Sintattic validation"));
			details.add(Pair.of("category3", "Semantic validation"));
			details.add(Pair.of("category4", "Message interpretation"));
			details.add(Pair.of("category5", "Datamodel mapping"));

			details.add(Pair.of("Connection type", "Kafka"));
			details.add(Pair.of("Sintattic validation", "One+One"));
			details.add(Pair.of("Semantic validation", "Disabled"));
			details.add(Pair.of("Message interpretation", "xml"));
			details.add(Pair.of("Datamodel mapping", "MySQL database"));
			break;
		case "senml.jar":
			details.add(Pair.of("category1", "Connection type"));
			details.add(Pair.of("category2", "Sintattic validation"));
			details.add(Pair.of("category3", "Semantic validation"));
			details.add(Pair.of("category4", "Message interpretation"));
			details.add(Pair.of("category5", "Datamodel mapping"));

			details.add(Pair.of("Connection type", "Kafka"));
			details.add(Pair.of("Sintattic validation", "Disabled"));
			details.add(Pair.of("Semantic validation", "Disabled"));
			details.add(Pair.of("Message interpretation", "senml"));
			details.add(Pair.of("Datamodel mapping", "One to one"));
			break;
		}
		return details;
	}

	@Override
	public void removeLoadedJAR(String toBeRemovedJAR) {
		loadedJars.remove(toBeRemovedJAR);

	}

	
	
}

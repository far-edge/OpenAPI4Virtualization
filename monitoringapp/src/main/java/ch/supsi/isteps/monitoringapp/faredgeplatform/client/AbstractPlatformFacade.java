package ch.supsi.isteps.monitoringapp.faredgeplatform.client;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;

import ch.supsi.isteps.monitoringapp.tools.AbstractElement;
import ch.supsi.isteps.monitoringapp.tools.CPS;
import ch.supsi.isteps.monitoringapp.tools.SmartObject;


public abstract class AbstractPlatformFacade {

	public abstract boolean isConnected();
	
	public abstract void disconnect();

	public abstract void connect();

	public abstract void setup();

	public abstract void clearAll();
	
	public abstract boolean compareCredentials(String username, String password);
			
	public abstract String retrieveSensorElements();

	public abstract String retrieveSensorDataElements(String anArchetype);

	public abstract String retrieveAttributesByElement(String sensorId);

	public abstract void createSensor(String newSensor) throws Exception;

	public abstract CPS getSensor(String sensorName);
	
	public abstract List<String> getExistingSensors();

	public abstract void updateSensor(AbstractElement sensor);

	public abstract void deleteSensor(String toBeRemovedSensor);

	public abstract void createSmartObject(String newSmartObject);

	public abstract SmartObject getSmartObject(String SmartObjectName);

	public abstract List<String> getSmartObjects();

	public abstract void updateSmartObject(AbstractElement smartObject);

	public abstract void deleteSmartObject(String toBeRemovedSmartObject);

	public abstract List<String> getPairedSensors();
	
	public abstract void savePairedSensor(String newPairedSensor, String jarName);
	
	public abstract void deletePairedSensor(String toBeRemovedSensor);
	
	public abstract List<String> getPairings();
	
	public abstract void savePairing(String newPairing);
	
	public abstract void removePairing(String pairingToBeRemoved);
	
	public abstract List<String> getActiveSensors();
	
	public abstract void saveActiveSensor(List<String> activeSensors);
	
	public abstract void uploadBAOS(ByteArrayOutputStream os, String filename); 
	
	public abstract List<String> getJars();
	
	public abstract ArrayList<Pair<String, String>> getJarDetails(String configurationName);

	public abstract FileResource downloadConfigurationTemplate();

	public abstract void removeLoadedJAR(String toBeRemovedJAR);

}
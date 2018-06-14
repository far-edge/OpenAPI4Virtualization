package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ch.supsi.isteps.virtualfactory.tools.XMap;

public class TestFaredgeKafkaClient extends AbstractFaredgeApiClient {

	private List<String> _sensorList;
	private ArrayList<Pair<String, String>> _sensors;
	private XMap<Boolean> _isSubscribed;

	public TestFaredgeKafkaClient() {
		_sensorList = new ArrayList<String>(); 
		_sensors = new ArrayList<Pair<String, String>>();
		_isSubscribed = new XMap<Boolean>(false);
	}
	
	@Override
	public List<String> retrieveActiveSensors() {
		return _sensorList;
	}

	@Override
	public void subscribe(String sensorName, AbstractEventHandler eventHandler) {
		for (Pair<String,String> eachPair : _sensors) {
			if(eachPair.getKey().equals(sensorName)) {
				eventHandler.notify(eachPair.getKey(), eachPair.getValue());
			}
		}
		_isSubscribed.put(sensorName, true);
	}

	@Override
	public void unscribe(String sensorName) {
		_isSubscribed.put(sensorName, false);
	}

	public void addSensor(String name) {
		_sensorList.add(name);
	}
	
	public void addSensorMessage(String key, String value) {
		_sensors.add(Pair.of(key, value));
	}
	
	public boolean isSuscribed(String aName) {
		return _isSubscribed.named(aName);
	}

	public void clear() {
		_isSubscribed = new XMap<Boolean>(false);
		_sensorList = new ArrayList<String>();
		_sensors = new ArrayList<Pair<String, String>>();
	}
}

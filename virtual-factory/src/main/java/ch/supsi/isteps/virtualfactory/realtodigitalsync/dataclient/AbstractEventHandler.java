package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

public abstract class AbstractEventHandler {

	// key=name of the sensor
	// value = SenML message
	public abstract void notify(String aKey, String aValue);
}

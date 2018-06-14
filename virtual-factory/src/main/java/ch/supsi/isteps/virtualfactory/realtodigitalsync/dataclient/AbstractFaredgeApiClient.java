package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.List;

public abstract class AbstractFaredgeApiClient {

	public abstract List<String> retrieveActiveSensors();

	public abstract void subscribe(String sensorName, AbstractEventHandler eventHandler);

	public abstract void unscribe(String name);

}

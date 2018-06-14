package ch.supsi.isteps.monitoringapp.faredgeplatform.client;

import ch.supsi.isteps.monitoringapp.data.ConfigurationData;

public class SingletonFacade {

	private static AbstractPlatformFacade instance;

	public static AbstractPlatformFacade getInstance() {
		if (instance == null) {
			//instance = new FakePlatformFacade();
			instance = new FaredgePlatformFacade(ConfigurationData.REAL_TO_DIGITAL_SYNCHRONIZATION_URL);
		}
		return instance;
	}
}
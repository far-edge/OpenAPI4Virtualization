package ch.supsi.isteps.virtualfactory.realtodigitalsync.data;

public class ConfigurationData {

	public static final String KAFKA_URL = "kafka:9092";
	public static final String MOCK_UP_SETUP_URL = "http://sensordataproducer:8070/setup";
	//port configured in the application.properties file because of @Profile configuration
	public static final String REAL_TO_DIGITAL_SYNCHRONIZATION_COMPONENT_PORT = "8090";

}

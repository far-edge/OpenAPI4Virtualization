package ch.supsi.isteps.prototype.commons;

import java.util.HashMap;
import java.util.Map;

import ch.supsi.isteps.prototype.data.StepData;

public class LoadJsonSchemaStep extends AbstractPersonalizedStep {

	private Map<String, String> _jsonMLSchema;

	public LoadJsonSchemaStep() {
		_jsonMLSchema= initJsonSenMLSchema();
	}

	private Map<String, String> initJsonSenMLSchema() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("BOL", bolJsonSchema());
		result.put("EOLGR", eolJsonSchema("EOLGR"));
		result.put("EOLST", eolJsonSchema("EOLST"));
		return result;
	}

	private String eolJsonSchema(String aType) {
		return "{" + "\"type\":\"array\"," + "\"items\": [" + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"bn\": { \"enum\": [\"" + aType + "\"] }," + "\"bt\": { \"type\": \"number\" }" + "},"
				+ "\"required\": [\"bn\", \"bt\"]" + "}," + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"n\": { \"enum\": [\"serialNumber\"] }," + "\"u\": { \"enum\": [\"vs\"] },"
				+ "\"v\": { \"type\": \"string\" }" + "}," + "\"required\": [\"n\", \"u\", \"v\"]" + "}," + "{"
				+ "\"type\":\"object\"," + "\"properties\": {" + "\"n\": { \"enum\": [\"batchNumber\"] },"
				+ "\"u\": { \"enum\": [\"vs\"] }," + "\"v\": { \"type\": \"string\" }" + "},"
				+ "\"required\": [\"n\", \"u\", \"v\"]" + "}," + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"n\": { \"enum\": [\"sku\"] }," + "\"u\": { \"enum\": [\"vs\"] },"
				+ "\"v\": { \"type\": \"string\" }" + "}," + "\"required\": [\"n\", \"u\", \"v\"]" + "}," + "{"
				+ "\"type\":\"object\"," + "\"properties\": {" + "\"n\": { \"enum\": [\"productionLine\"] },"
				+ "\"u\": { \"enum\": [\"vs\"] }," + "\"v\": { \"type\": \"string\" }" + "},"
				+ "\"required\": [\"n\", \"u\", \"v\"]" + "}]," + "\"additionalProperties\": false" + "}";
	}

	private String bolJsonSchema() {
		return "{" + "\"type\":\"array\"," + "\"items\": [" + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"bn\": { \"enum\": [\"BOL\"] }," + "\"bt\": { \"type\": \"number\" }" + "},"
				+ "\"required\": [\"bn\", \"bt\"]" + "}," + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"n\": { \"enum\": [\"industrialCode\"] }," + "\"u\": { \"enum\": [\"vs\"] },"
				+ "\"v\": { \"type\": \"string\" }" + "}," + "\"required\": [\"n\", \"u\", \"v\"]" + "}," + "{"
				+ "\"type\":\"object\"," + "\"properties\": {" + "\"n\": { \"enum\": [\"serialNumber\"] },"
				+ "\"u\": { \"enum\": [\"vs\"] }," + "\"v\": { \"type\": \"string\" }" + "},"
				+ "\"required\": [\"n\", \"u\", \"v\"]" + "}," + "{" + "\"type\":\"object\"," + "\"properties\": {"
				+ "\"n\": { \"enum\": [\"productionLine\"] }," + "\"u\": { \"enum\": [\"vs\"] },"
				+ "\"v\": { \"type\": \"string\" }" + "}," + "\"required\": [\"n\", \"u\", \"v\"]" + "}],"
				+ "\"additionalProperties\": false" + "}";
	}

	@Override
	public Map<String, String> execute(Map<String, String> anInput) {
		HashMap<String, String> result = new HashMap<String, String>();
		if(!anInput.containsKey(StepData.SENSOR_NAME)) {
			result.put(StepData.MESSAGE, "mandatory input named " + StepData.SENSOR_NAME);
			return result; 
		}
		String sensorName = anInput.get(StepData.SENSOR_NAME);
		String key = "no key";
		if (sensorName.startsWith("BOL"))
			key = "BOL";
		if (sensorName.startsWith("EOLGR"))
			key = "EOLGR";
		if (sensorName.startsWith("EOLST"))
			key = "EOLST";
		if(!_jsonMLSchema.containsKey(key)) return new HashMap<String, String>();
		String jsonSchema = _jsonMLSchema.get(key);
		if (jsonSchema.isEmpty()) {
			result.put("message", "no json schema found");
			return result;
		}
		result.put("jsonSchema", jsonSchema);
		return result;
	}
}
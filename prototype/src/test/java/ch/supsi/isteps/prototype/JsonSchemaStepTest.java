package ch.supsi.isteps.prototype;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.data.StepData;

public class JsonSchemaStepTest {

	@Test
	public void execute_emptyParameters_errorMessage() {
		AbstractPersonalizedStep jsonSchemaValidator = CommonPersonalizedStepFactory.createJsonSchemaValidator();
		Map<String, String> result = jsonSchemaValidator.execute(new HashMap<String, String>());
		assertEquals(1, result.size());
		assertEquals("message", result.keySet().iterator().next());
		assertEquals("mandatory input named "+ StepData.SENSOR_NAME, result.get(StepData.MESSAGE));
	}
	
	@Test
	public void execute_parsedMessage() {
		AbstractPersonalizedStep jsonSchemaValidator = CommonPersonalizedStepFactory.createJsonSchemaValidator();
		HashMap<String, String> input = new HashMap<String, String>();
		input.put(StepData.SENSOR_NAME, "BOL");
		input.put(StepData.MESSAGE, whr_BOL("G1"));
		Map<String, String> result = jsonSchemaValidator.execute(input);
		assertEquals(2, result.size());
		Iterator<String> iterator = result.keySet().iterator();
		assertEquals("jsonSchema", iterator.next());
		assertEquals("outcome", iterator.next());
		assertEquals("true", result.get("outcome"));
	}
	
	public static String whr_BOL(String productionLine) {
		String result = "[" +
				"{\"bn\": \"BOL\", \"bt\": 1276020076}," +
				"{\"n\": \"industrialCode\", \"u\": \"vs\", \"v\": \"944934370000\"}," +
				"{\"n\": \"serialNumber\", \"u\": \"vs\", \"v\": \"901702003358\"}," +
				"{\"n\": \"productionLine\", \"u\": \"vs\", \"v\": \""+ productionLine +"\"}" +
			"]";
		return result;
}
}
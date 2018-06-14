package ch.supsi.isteps.prototype.whirlpool;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import ch.supsi.isteps.prototype.commons.AbstractJsonParsingLogicStrategy;
import ch.supsi.isteps.prototype.data.StepData;

public class WhirlpoolParsingLogicStrategy extends AbstractJsonParsingLogicStrategy{

	@Override
	public void parse(Map<String, String> someFields, Iterator<Entry<String, JsonNode>> currentElement) {
		String previousKey = "";
		while (currentElement.hasNext()) {
			Entry<String, JsonNode> next = currentElement.next();
			String key = next.getKey();
			String value = next.getValue().asText();
			if (key.equals("bn")) {
				someFields.put(StepData.SENSOR_NAME, value);
			} else if (key.equals("bt")) {
				someFields.put(StepData.TIME_STAMP, value);
			} else if (key.equals("n")) {
				previousKey = value;
			} else if (key.equals("v")) {
				someFields.put(previousKey, value);
			}
		}
	}
}
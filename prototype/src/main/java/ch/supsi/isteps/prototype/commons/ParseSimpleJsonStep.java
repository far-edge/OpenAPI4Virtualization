package ch.supsi.isteps.prototype.commons;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.supsi.isteps.prototype.data.StepData;

public class ParseSimpleJsonStep extends AbstractPersonalizedStep{

	private AbstractJsonParsingLogicStrategy _parsingLogicStrategy;
	
	public ParseSimpleJsonStep(AbstractJsonParsingLogicStrategy aStrategy) {
		_parsingLogicStrategy = aStrategy;
	}

	@Override
	public Map<String, String> execute(Map<String, String> anInput) {
		if(!anInput.containsKey(StepData.MESSAGE)) return new HashMap<String, String>();
		Map<String, String> result = new HashMap<String, String>();
		String jsonMessage = anInput.get(StepData.MESSAGE);
		Map<String, String> someFields = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(jsonMessage.getBytes());
			Iterator<JsonNode> elements = jsonNode.elements();
			while(elements.hasNext()) {
				JsonNode node = elements.next();
				Iterator<Entry<String, JsonNode>> currentElement = node.fields();
			    _parsingLogicStrategy.parse(someFields, currentElement);
			}
		} catch (IOException e) {
			result.put("outcome", "true");
			e.printStackTrace();
		}
		result.putAll(someFields);
		return result;
	}
}

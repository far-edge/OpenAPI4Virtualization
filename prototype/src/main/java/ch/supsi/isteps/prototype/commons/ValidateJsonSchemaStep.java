package ch.supsi.isteps.prototype.commons;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;

import ch.supsi.isteps.prototype.data.StepData;

public class ValidateJsonSchemaStep extends AbstractPersonalizedStep {

	private JsonSchemaFactory _jsonSchemaFactory;

	public ValidateJsonSchemaStep(JsonSchemaFactory jsonSchemaFactory) {
		_jsonSchemaFactory = jsonSchemaFactory;
	}

	@Override
	public Map<String, String> execute(Map<String, String> anInput) {
		if(!anInput.containsKey(StepData.JSON_SCHEMA)) return new HashMap<String, String>();
		if(!anInput.containsKey(StepData.MESSAGE)) return new HashMap<String, String>();
		HashMap<String, String> result = new HashMap<String, String>();
		String schemaContent = anInput.get(StepData.JSON_SCHEMA);
		JsonSchema schema = _jsonSchemaFactory.getSchema(schemaContent);
		ObjectMapper mapper = new ObjectMapper();
        try {
			String request = anInput.get(StepData.MESSAGE);
			JsonNode node = mapper.readTree(request);
			Set<ValidationMessage> errors = schema.validate(node);
			if(errors.size() == 0){
				result.put("outcome", "true");
				return result;
			}else{
				result.put("outcome", "false");
				result.put("message", errors.iterator().next().getMessage());
				return result;
			}
		} catch (IOException e) {
			result.put("outcome", "false");
			result.put("message", e.getMessage());
		}
        return result;
	}
}

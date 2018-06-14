package ch.supsi.isteps.prototype.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.supsi.isteps.prototype.CommonPersonalizedStepFactory;
import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.commons.DoNothingStep;
import ch.supsi.isteps.prototype.data.StepData;
import ch.supsi.isteps.prototype.whirlpool.WhirlpoolParsingLogicStrategy;
import ch.supsi.isteps.prototype.whirlpool.WhirlpoolPersonalizedStepFactory;

public class PlatformComponentFactory extends AbstractComponentFactory{

	public PlatformComponentFactory() {
		super();
	}

	@Override
	public Map<String, AbstractPersonalizedStep> create(AbstractOpenAPIForVirtualization aProxy) {
		put(StepData.SYNTAX_VALIDATION, CommonPersonalizedStepFactory.createJsonSchemaValidator());
		put(StepData.SEMANTIC_VALIDATION, new DoNothingStep());
		put(StepData.DATA_TRANSLATION, CommonPersonalizedStepFactory.createSimpleJsonParser(new WhirlpoolParsingLogicStrategy()));
		put(StepData.MODEL_UPDATER, WhirlpoolPersonalizedStepFactory.createDataModelMapper(aProxy));
		return super.result();
	}

	@Override
	public Map<String, String> descriptionAsMap() {
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		result.put("category1", "Connection type");
		result.put("category2", "Syntax validation");
		result.put("category3", "Semantic validation");
		result.put("category4", "Data Translation");
		result.put("category5", "Datamodel mapping");
		result.put("Connection type", "Kafka Channel");
		result.put("Syntax validation", "Json");
		result.put("Semantic validation", "SenML");
		result.put("Data Translation", "Nothing to do");
		result.put("Datamodel mapping", "Whirlpool Data Model");
		return result;
	}
	
}
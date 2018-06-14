package ch.supsi.isteps.prototype.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.commons.DoNothingStep;
import ch.supsi.isteps.prototype.data.StepData;

public abstract class AbstractComponentFactory {

	private HashMap<String, AbstractPersonalizedStep> _result;
	
	public AbstractComponentFactory() {
		 _result = new HashMap<String, AbstractPersonalizedStep>();
		_result.put(StepData.SYNTAX_VALIDATION, new DoNothingStep());
		_result.put(StepData.SEMANTIC_VALIDATION, new DoNothingStep());
		_result.put(StepData.DATA_TRANSLATION, new DoNothingStep());
		_result.put(StepData.MODEL_UPDATER, new DoNothingStep());
	}
	
	public void put(String aKey, AbstractPersonalizedStep aStep) {
		_result.put(aKey, aStep);
	}
	
	public List<String> mandatoryKeys(){
		return Arrays.asList(StepData.SYNTAX_VALIDATION, StepData.SEMANTIC_VALIDATION, StepData.DATA_TRANSLATION, StepData.MODEL_UPDATER);
	}

	public abstract Map<String, AbstractPersonalizedStep> create(AbstractOpenAPIForVirtualization aProxy);

	public Map<String, AbstractPersonalizedStep> result() {
		return _result;
	}

	public abstract Map<String, String> descriptionAsMap();
}
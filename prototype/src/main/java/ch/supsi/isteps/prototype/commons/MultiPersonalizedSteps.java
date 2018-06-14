package ch.supsi.isteps.prototype.commons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiPersonalizedSteps extends AbstractPersonalizedStep {

	private List<AbstractPersonalizedStep> _someSteps;

	public MultiPersonalizedSteps(AbstractPersonalizedStep...someSteps) {
		_someSteps = Arrays.asList(someSteps);
	}
	
	@Override
	public Map<String, String> execute(Map<String, String> anInput) {
		Map<String, String> result = new HashMap<String, String>();
		for (AbstractPersonalizedStep each : _someSteps) {
			result.putAll(each.execute(anInput));
			anInput.putAll(result);
		}
		return result;
	}
}
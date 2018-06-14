package ch.supsi.isteps.prototype.commons;

import java.util.Map;

public class DoNothingStep extends AbstractPersonalizedStep{

	@Override
	public Map<String, String> execute(Map<String, String> anInput) {
		System.out.println("DO NOTHING");
		return anInput;
	}
}
package ch.supsi.isteps.prototype.commons;

import java.util.Map;

public abstract class AbstractPersonalizedStep {

	public abstract Map<String, String> execute(Map<String, String> someParameters);
}
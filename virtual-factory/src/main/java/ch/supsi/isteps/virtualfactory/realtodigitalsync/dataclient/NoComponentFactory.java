package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.LinkedHashMap;
import java.util.Map;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.tools.AbstractComponentFactory;
import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;

public class NoComponentFactory extends AbstractComponentFactory {

	@Override
	public Map<String, AbstractPersonalizedStep> create(AbstractOpenAPIForVirtualization aProxy) {
		return result();
	}

	@Override
	public Map<String, String> descriptionAsMap() {
		return new LinkedHashMap<String, String>();
	}
}
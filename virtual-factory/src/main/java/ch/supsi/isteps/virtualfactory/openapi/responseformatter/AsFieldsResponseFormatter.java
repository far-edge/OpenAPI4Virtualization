package ch.supsi.isteps.virtualfactory.openapi.responseformatter;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public class AsFieldsResponseFormatter extends AbstractResponseFormatter{

	@Override
	public String format(Fields retrieveAllElementsByLayerOutput) {
		return retrieveAllElementsByLayerOutput.toRaw();
	}
}
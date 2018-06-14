package ch.supsi.isteps.virtualfactory.openapi.responseformatter;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public abstract class AbstractResponseFormatter {

	public abstract String format(Fields retrieveAllElementsByLayerOutput);
}
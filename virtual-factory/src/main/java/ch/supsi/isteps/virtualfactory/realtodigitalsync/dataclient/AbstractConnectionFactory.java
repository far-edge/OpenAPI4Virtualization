package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;

public abstract class AbstractConnectionFactory {

	public abstract XSystem createUsing(Fields someFields);

}

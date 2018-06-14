package ch.supsi.isteps.prototype.whirlpool;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;

public class WhirlpoolPersonalizedStepFactory {

	public static AbstractPersonalizedStep createDataModelMapper(AbstractOpenAPIForVirtualization dataModelProxy) {
		return new WhirlpoolDataModelMapperStep(dataModelProxy);
	}
}
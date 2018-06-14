package ch.supsi.isteps.prototype;

import com.networknt.schema.JsonSchemaFactory;

import ch.supsi.isteps.prototype.commons.AbstractJsonParsingLogicStrategy;
import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.commons.DoNothingStep;
import ch.supsi.isteps.prototype.commons.LoadJsonSchemaStep;
import ch.supsi.isteps.prototype.commons.MultiPersonalizedSteps;
import ch.supsi.isteps.prototype.commons.ParseSimpleJsonStep;
import ch.supsi.isteps.prototype.commons.ValidateJsonSchemaStep;

public class CommonPersonalizedStepFactory {

	public static AbstractPersonalizedStep createJsonSchemaValidator() {
		return new MultiPersonalizedSteps(new LoadJsonSchemaStep(), new ValidateJsonSchemaStep(new JsonSchemaFactory()));
	}

	public static AbstractPersonalizedStep doNothing() {
		return new DoNothingStep();
	}

	public static AbstractPersonalizedStep createSimpleJsonParser(AbstractJsonParsingLogicStrategy aParsingLogic) {
		return new ParseSimpleJsonStep(aParsingLogic);
	}
}
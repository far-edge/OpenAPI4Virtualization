package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command;

import java.util.Map;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class StepToCommandAdapterCommand extends AbstractCommand {

	private AbstractPersonalizedStep _step;

	public StepToCommandAdapterCommand(AbstractPersonalizedStep step) {
		_step = step;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Map<String, String> input = Fields.toMap(anInput);
		System.out.println("INPUT "+ input.keySet());
		Map<String, String> result = _step.execute(input);
		System.out.println("EACH RESULT "+ result.keySet());
		anOutput.putAll(Fields.fromMap(result));
	}
}
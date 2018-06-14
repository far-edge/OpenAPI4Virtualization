package ch.supsi.isteps.virtualfactory.tools.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public class UnknownCommand extends AbstractCommand {

	private String _responseKey;

	public UnknownCommand(String responseKey) {
		_responseKey = responseKey;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		anOutput.put(_responseKey, "unknown Command");
	}

}

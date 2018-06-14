package ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class ConnectionStatusCommand extends AbstractCommand {

	private Fields _configuration;

	public ConnectionStatusCommand(Fields configuration) {
		_configuration = configuration;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		anOutput.put("outcome", "true");
		anOutput.putAll(_configuration.select("status"));
	}
}
package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.logger.PrintStreamLogger;

public class LogIOCommand extends AbstractCommand {

	private AbstractCommand _command;
	private String _description;
	private PrintStreamLogger _logger;

	public LogIOCommand(String description, AbstractCommand command) {
		_description = description;
		_command = command;
		_logger = new PrintStreamLogger(System.out);
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_logger.log(_description + " INPUT: "+ anInput.toRaw());
		_command.execute(anInput, anOutput);
		_logger.log(_description + " OUTPUT: "+ anInput.toRaw());
	}
}
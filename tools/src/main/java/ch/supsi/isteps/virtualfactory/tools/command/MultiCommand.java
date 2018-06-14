package ch.supsi.isteps.virtualfactory.tools.command;

import java.util.ArrayList;
import java.util.List;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public class MultiCommand extends AbstractCommand{

	private List<AbstractCommand> _commands;

	public MultiCommand() {
		_commands = new ArrayList<AbstractCommand>();
	}
	
	@Override
	public void execute(Fields anInput, Fields anOutput) {
		for (AbstractCommand each : _commands) {
			each.execute(anInput, anOutput);
		}
	}

	public void add(AbstractCommand aCommand) {
		_commands.add(aCommand);
	}
}

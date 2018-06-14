package ch.supsi.isteps.virtualfactory.tools.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;

public class CallXSystemCommand extends AbstractCommand {

	private XSystem _system;

	public CallXSystemCommand(XSystem system) {
		_system = system;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_system.execute(anInput, anOutput);
	}
}
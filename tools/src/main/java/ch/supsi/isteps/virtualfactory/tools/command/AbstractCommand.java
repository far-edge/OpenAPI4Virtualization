package ch.supsi.isteps.virtualfactory.tools.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public abstract class AbstractCommand {

	public abstract void execute(Fields anInput, Fields anOutput);
}

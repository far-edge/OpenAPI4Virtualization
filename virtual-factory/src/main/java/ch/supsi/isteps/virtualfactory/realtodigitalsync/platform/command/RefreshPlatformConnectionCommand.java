package ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class RefreshPlatformConnectionCommand extends AbstractCommand {

	private XSystem _platformSystem;

	public RefreshPlatformConnectionCommand(XSystem platformSystem) {
		_platformSystem = platformSystem;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_platformSystem.execute(Fields.fromRaw("commandName=disconnectPlatform"), Fields.empty());
		_platformSystem.execute(Fields.fromRaw("commandName=connectPlatform"), Fields.empty());
	}
}

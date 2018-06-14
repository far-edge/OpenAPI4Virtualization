package ch.supsi.isteps.virtualfactory.triggers.command;

import java.util.List;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class ApplyTriggerCommand extends AbstractCommand {

	private XSystem _dataModel;
	private XMap<Fields> _triggers;
	private XMap<XSystem> _proxies;

	public ApplyTriggerCommand(XMap<Fields> triggers, XMap<XSystem> proxies, XSystem dataModel) {
		_triggers = triggers;
		_proxies = proxies;
		_dataModel = dataModel;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields someFields = _triggers.named(anInput.firstValueFor("called"));
		String onEvent = someFields.firstValueFor("onEvent");
		String callback = someFields.firstValueFor("callback");
		List<String> keys = someFields.allKeysStartingWith("checkKeys");
		CommandHandler commandHandler = _dataModel.commandHandler();
		AbstractCommand regularCommand = commandHandler.named(onEvent);
		commandHandler.put(onEvent, new CallbackCommand(keys, someFields, _proxies.named(callback), regularCommand));
		anOutput.put("outcome", "true");
	}
}
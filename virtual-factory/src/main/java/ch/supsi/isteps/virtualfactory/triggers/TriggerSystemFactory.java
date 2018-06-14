package ch.supsi.isteps.virtualfactory.triggers;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;
import ch.supsi.isteps.virtualfactory.triggers.command.ApplyTriggerCommand;
import ch.supsi.isteps.virtualfactory.triggers.command.RegisterTriggerCommand;
import ch.supsi.isteps.virtualfactory.triggers.command.ResetTriggerCommand;

public class TriggerSystemFactory {

	public static XSystem createUsing(RemoteProxyFactory remoteProxyFactory, XMap<XSystem> proxies, XSystem dataModel, XSystem originalDataModel) {
		XMap<Fields> triggers = new XMap<Fields>(Fields.empty());
		
		CommandHandler commandHandler = new CommandHandler();
		commandHandler.put(TriggerCommandData.REGISTER_TRIGGER, new RegisterTriggerCommand(remoteProxyFactory, proxies, triggers));
		commandHandler.put(TriggerCommandData.APPLY_TRIGGER, new ApplyTriggerCommand(triggers, proxies, dataModel));
		commandHandler.put(TriggerCommandData.RESET_TRIGGER, new ResetTriggerCommand(triggers, new ApplyTriggerCommand(triggers, proxies, dataModel), dataModel, originalDataModel));
		return new XSystem(commandHandler);
	}
}
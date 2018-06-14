package ch.supsi.isteps.virtualfactory.realtodigitalsync;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.command.LoadSensorConfigurationCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel0;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.data.ConfiguratorCommandData;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class ConfiguratorSystemFactory {

	public static XSystem create(OpenAPIForVirtualizationLevel0 openAPIForVirtualizationLevel0AsFields) {
		CommandHandler configuratorCommandHandler = new CommandHandler();
		configuratorCommandHandler.put(ConfiguratorCommandData.LOAD_CONFIGURATION_FOR, new LoadSensorConfigurationCommand(openAPIForVirtualizationLevel0AsFields));
		return new XSystem("Configurator", configuratorCommandHandler);
	}
}
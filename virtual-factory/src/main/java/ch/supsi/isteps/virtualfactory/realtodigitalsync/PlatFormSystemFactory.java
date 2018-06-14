package ch.supsi.isteps.virtualfactory.realtodigitalsync;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.FaredgePlatformCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command.ConnectPlatformCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command.ConnectionStatusCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command.DisconnectPlatformCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command.RefreshPlatformConnectionCommand;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class PlatFormSystemFactory {

	public static XSystem create(XSystem faredgeApi, XSystem configurator) {
		Fields configuration = Fields.single("status", "disconnected");
		CommandHandler commandHandler = new CommandHandler();
		XSystem result = new XSystem("Platform", commandHandler);
		commandHandler.put(FaredgePlatformCommandData.CONNECT_PLATFORM, new ConnectPlatformCommand(faredgeApi, configurator, configuration));
		commandHandler.put(FaredgePlatformCommandData.DISCONNECT_PLATFORM, new DisconnectPlatformCommand(faredgeApi, configuration));
		commandHandler.put(FaredgePlatformCommandData.REFRESH_PLATFORM_CONNECTION, new RefreshPlatformConnectionCommand(result));
		commandHandler.put(FaredgePlatformCommandData.CONNECTION_STATUS, new ConnectionStatusCommand(configuration));
		return result;
	}
}
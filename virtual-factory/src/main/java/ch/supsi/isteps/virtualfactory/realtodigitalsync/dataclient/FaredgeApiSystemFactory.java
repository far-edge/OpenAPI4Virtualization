package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command.RetrieveSensorListCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command.SubscribeSensorCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command.UnsubscribeSensorCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.data.FarEdgeApiCommandData;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class FaredgeApiSystemFactory {

	public static XSystem create(AbstractFaredgeApiClient faredgeApiClient, AbstractConnectionFactory connectionFactory) {
		CommandHandler faredgeApiCommandHandler = new CommandHandler();
		faredgeApiCommandHandler.put(FarEdgeApiCommandData.RETRIEVE_SENSOR_LIST, new RetrieveSensorListCommand(faredgeApiClient));
		faredgeApiCommandHandler.put(FarEdgeApiCommandData.SUBSCRIBE_SENSOR, new SubscribeSensorCommand(connectionFactory, faredgeApiClient));
		faredgeApiCommandHandler.put(FarEdgeApiCommandData.UNSUBSCRIBE_SENSOR, new UnsubscribeSensorCommand(faredgeApiClient));
		return new XSystem("FarEdge Api", faredgeApiCommandHandler);
	}
}
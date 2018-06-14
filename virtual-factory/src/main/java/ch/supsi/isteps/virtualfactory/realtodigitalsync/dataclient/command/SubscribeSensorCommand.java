package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.AbstractConnectionFactory;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.AbstractFaredgeApiClient;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.FaredgeEventHandler;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class SubscribeSensorCommand extends AbstractCommand {

	private AbstractConnectionFactory _connectionFactory;
	private AbstractFaredgeApiClient _kafkaClient;

	public SubscribeSensorCommand(AbstractConnectionFactory connectionFactory, AbstractFaredgeApiClient kafkaClient) {
		_connectionFactory = connectionFactory;
		_kafkaClient = kafkaClient;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		XSystem realToDigitalSynchronization = _connectionFactory.createUsing(anInput.select(RealToDigitalSyncData.SENSOR_NAME, RealToDigitalSyncData.RESOURCE_PATH));
		_kafkaClient.subscribe(anInput.firstValueFor(RealToDigitalSyncData.SENSOR_NAME), new FaredgeEventHandler(realToDigitalSynchronization));
		anOutput.put(ToolData.OUTCOME, ConstantData.TRUE);
	}
}
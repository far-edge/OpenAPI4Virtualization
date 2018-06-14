package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.AbstractFaredgeApiClient;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class UnsubscribeSensorCommand extends AbstractCommand {

	private AbstractFaredgeApiClient _kafkaClient;

	public UnsubscribeSensorCommand(AbstractFaredgeApiClient kafkaClient) {
		_kafkaClient = kafkaClient;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_kafkaClient.unscribe(anInput.firstValueFor(RealToDigitalSyncData.SENSOR_NAME));
		anOutput.put(ToolData.OUTCOME, ConstantData.TRUE);
	}
}
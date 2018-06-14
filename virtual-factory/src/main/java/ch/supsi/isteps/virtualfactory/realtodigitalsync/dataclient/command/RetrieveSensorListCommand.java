package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command;

import java.util.List;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.AbstractFaredgeApiClient;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class RetrieveSensorListCommand extends AbstractCommand {

	private AbstractFaredgeApiClient _kafkaClient;

	public RetrieveSensorListCommand(AbstractFaredgeApiClient kafkaClient) {
		_kafkaClient = kafkaClient;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		List<String> sensors = _kafkaClient.retrieveActiveSensors();
		anOutput.put(ToolData.OUTCOME, ConstantData.TRUE);
		anOutput.put(ToolData.COUNT, String.valueOf(sensors.size()));
		for (String each : sensors) {
			anOutput.put(RealToDigitalSyncData.SENSOR_NAME, each);
		}
	}
}
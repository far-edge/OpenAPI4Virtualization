package ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command;

import java.util.List;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.data.FarEdgeApiCommandData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class DisconnectPlatformCommand extends AbstractCommand {

	private XSystem _edgeDataSourceSystem;
	private Fields _configuration;

	public DisconnectPlatformCommand(XSystem edgeDataSourceSystem, Fields configuration) {
		_edgeDataSourceSystem = edgeDataSourceSystem;
		_configuration = configuration;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields retrieveListOutput = Fields.empty();
		_edgeDataSourceSystem.execute(Fields.fromRaw("commandName=retrieveSensorList"), retrieveListOutput);
		List<String> sensorKeys = retrieveListOutput.allKeysStartingWith("sensor.name");
		for (String each : sensorKeys) {
			_edgeDataSourceSystem.execute(Fields.fromRaw("commandName="+ FarEdgeApiCommandData.UNSUBSCRIBE_SENSOR +"|sensorName="+ retrieveListOutput.firstValueFor(each)), Fields.empty());
		}
		_configuration.clear();
		_configuration.put("status", "disconnected");
	}
}
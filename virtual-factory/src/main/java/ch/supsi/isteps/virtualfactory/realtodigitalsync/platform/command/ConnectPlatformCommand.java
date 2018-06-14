package ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command;

import java.util.List;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.data.FarEdgeApiCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.data.ConfiguratorCommandData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class ConnectPlatformCommand extends AbstractCommand {

	private XSystem _edgeDataSourceSystem;
	private XSystem _configurator;
	private Fields _configuration;

	public ConnectPlatformCommand(XSystem edgeDataSourceSystem, XSystem configuratorPersistenceSystem, Fields configuration) {
		_edgeDataSourceSystem = edgeDataSourceSystem;
		_configurator = configuratorPersistenceSystem;
		_configuration = configuration;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields retrieveListOutput = Fields.empty();
		_edgeDataSourceSystem.execute(Fields.single(ToolData.COMMAND_NAME, FarEdgeApiCommandData.RETRIEVE_SENSOR_LIST), retrieveListOutput);
		List<String> sensorKeys = retrieveListOutput.allKeysStartingWith(RealToDigitalSyncData.SENSOR_NAME);
		
		for (String each : sensorKeys) {
			String sensorName = retrieveListOutput.firstValueFor(each);
			Fields loadConfigurationForOutput = Fields.empty();
			_configurator.execute(Fields.single(ToolData.COMMAND_NAME, ConfiguratorCommandData.LOAD_CONFIGURATION_FOR).put(RealToDigitalSyncData.SENSOR_NAME, sensorName), loadConfigurationForOutput);
			_edgeDataSourceSystem.execute(Fields.single(ToolData.COMMAND_NAME, FarEdgeApiCommandData.UNSUBSCRIBE_SENSOR).put(RealToDigitalSyncData.SENSOR_NAME, sensorName), Fields.empty());
			
			Fields input = Fields.empty();
			input.put(ToolData.COMMAND_NAME, FarEdgeApiCommandData.SUBSCRIBE_SENSOR);
			input.put(RealToDigitalSyncData.SENSOR_NAME, sensorName);
			input.putAll(loadConfigurationForOutput.select(RealToDigitalSyncData.RESOURCE_PATH));
			_edgeDataSourceSystem.execute(input, Fields.empty());
			
			_configuration.clear();
			_configuration.put(RealToDigitalSyncData.STATUS, "connected");
		}
	}
}
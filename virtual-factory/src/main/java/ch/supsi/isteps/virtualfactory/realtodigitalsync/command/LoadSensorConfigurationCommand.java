package ch.supsi.isteps.virtualfactory.realtodigitalsync.command;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel0;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class LoadSensorConfigurationCommand extends AbstractCommand {

	private OpenAPIForVirtualizationLevel0 _openAPIForVirtualizationLevel0AsFields;

	public LoadSensorConfigurationCommand(OpenAPIForVirtualizationLevel0 openAPIForVirtualizationLevel0AsFields) {
		_openAPIForVirtualizationLevel0AsFields = openAPIForVirtualizationLevel0AsFields;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		if(!anInput.containsKey(RealToDigitalSyncData.SENSOR_NAME)) {
			anOutput.put(ToolData.OUTCOME, "true");
			anOutput.put(ToolData.MESSAGE, "Missing key " + RealToDigitalSyncData.SENSOR_NAME);
			return;
		}
		anOutput.put(ToolData.OUTCOME, "true");
		//localhost configuration
		anOutput.put(RealToDigitalSyncData.RESOURCE_PATH, "file:src/main/resources/whirlpool.jar");
		//docker location
		//anOutput.put(RealToDigitalSyncData.RESOURCE_PATH, "file:whirlpool.jar");
	}
}
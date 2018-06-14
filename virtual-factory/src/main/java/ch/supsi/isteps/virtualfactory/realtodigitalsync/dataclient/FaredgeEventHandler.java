package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class FaredgeEventHandler extends AbstractEventHandler {

	private XSystem _realToDigitalSynchronization;

	public FaredgeEventHandler(XSystem realToDigitalSynchronization) {
		_realToDigitalSynchronization = realToDigitalSynchronization;
	}

	@Override
	public void notify(String aKey, String aValue) {
		Fields input = Fields.single(ToolData.COMMAND_NAME, RealToDigitalSyncCommandData.RUN);
		input.put(RealToDigitalSyncData.SENSOR_NAME, aKey);
		input.put(RealToDigitalSyncData.MESSAGE, aValue);
		Fields output = Fields.empty();
		_realToDigitalSynchronization.execute(input, output);
	}
}
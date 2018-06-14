package ch.supsi.isteps.prototype.whirlpool;

import java.util.HashMap;
import java.util.Map;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.data.StepData;
import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;


public class WhirlpoolDataModelMapperStep extends AbstractPersonalizedStep {

	private AbstractOpenAPIForVirtualization _openAPIForVirtualization;

	public WhirlpoolDataModelMapperStep(AbstractOpenAPIForVirtualization openAPIForVirtualization) {
		_openAPIForVirtualization = openAPIForVirtualization;
	}

	@Override
	public Map<String, String> execute(Map<String, String> someParameters) {
		if(!someParameters.containsKey(StepData.SENSOR_NAME)) return new HashMap<String, String>();
		if(!someParameters.containsKey(StepData.TIME_STAMP)) return new HashMap<String, String>();
		String sensorName = someParameters.get(StepData.SENSOR_NAME);
		String timestamp = someParameters.get(StepData.TIME_STAMP);
		String sensorRead = sensorName + timestamp;
		//save the element as a logical element - in case it's not existing it will be inserted otherwise it will report that the object is already existing and it will skip the insertion
		_openAPIForVirtualization.saveElement("logical", sensorName, sensorName);
		//insert the reading as a sensor element
		_openAPIForVirtualization.saveElement("sensor", sensorName, sensorRead);
		
		for (String each : someParameters.keySet()) {
			if(each.equals(StepData.SENSOR_NAME)) continue;
			_openAPIForVirtualization.saveAttribute("sensor", sensorRead, each, someParameters.get(each));
		}
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(StepData.OUTCOME, "true");
		return result;
	}
}
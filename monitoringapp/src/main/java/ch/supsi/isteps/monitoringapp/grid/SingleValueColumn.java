package ch.supsi.isteps.monitoringapp.grid;

public class SingleValueColumn {

	private String _sensorName;

	public SingleValueColumn(String sensorName) {
		_sensorName = sensorName;
	}
	
	public void setName(String name) {
		_sensorName = name;
	}

	public String getName() {
		return _sensorName;
	}
}

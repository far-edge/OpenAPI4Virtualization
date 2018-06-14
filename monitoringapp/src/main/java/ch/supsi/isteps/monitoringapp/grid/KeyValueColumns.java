package ch.supsi.isteps.monitoringapp.grid;

public class KeyValueColumns {

	private String _key;
	private String _value;
	
	public KeyValueColumns(String key, String value) {
		_key = key;
		_value = value;
	}

	public String getKey() {
		return _key;
	}

	public void setKey(String aKey) {
		_key = aKey;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String aValue) {
		_value = aValue;
	}
}
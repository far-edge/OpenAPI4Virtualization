package ch.supsi.isteps.virtualfactory.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XBag<T> {

	private Map<String, List<T>> _content;
	
	public XBag() {
		_content = new LinkedHashMap<String, List<T>>();
	}
	
	public void put(String aKey, T aValue) {
		if(!_content.containsKey(aKey)){
			_content.put(aKey, new ArrayList<T>());
		}
		_content.get(aKey).add(aValue);
	}

	public List<T> named(String aKey) {
		return _content.get(aKey);
	}

	public boolean containsKey(String aKey) {
		return _content.containsKey(aKey);
	}

	public List<String> keys() {
		return new ArrayList<String>(_content.keySet());
	}

	public List<T> asUniqueList() {
		ArrayList<T> result = new ArrayList<T>();
		for (String each : keys()) {
			result.addAll(named(each));
		}
		return result;
	}

}

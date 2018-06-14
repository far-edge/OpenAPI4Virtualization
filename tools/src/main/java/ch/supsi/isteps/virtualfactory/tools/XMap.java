package ch.supsi.isteps.virtualfactory.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class XMap<T> {

	private LinkedHashMap<String, T> _content;
	private T _default;
	
	public XMap(T defaultParam) {
		_content = new LinkedHashMap<String, T>();
		_default = defaultParam;
	}
	
	public void put(String aKey, T aValue) {
		_content.put(aKey, aValue);
	}

	public T named(String aKey) {
		if(!_content.containsKey(aKey)) return _default;
		return _content.get(aKey);
	}

	public int size() {
		return _content.size();
	}

	public List<String> keys() {
		return new ArrayList<String>(_content.keySet());
	}

	public boolean containsKey(String aKey) {
		return _content.containsKey(aKey);
	}
	
	@Override
	public String toString(){
		return "ciao";
	}

	public void remove(String each) {
		_content.remove(each);
	}

	public XMap<T> copy() {
		XMap<T> result = new XMap<T>(_default);
		for (String each : keys()) {
			result.put(each, named(each));
		}
		return result;
	}

	public void clear() {
		_content.clear();
		
	}
}
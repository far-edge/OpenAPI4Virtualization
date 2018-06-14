package ch.supsi.isteps.monitoringapp.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Fields implements Serializable{

	private LinkedHashMap<String, String> _content;

	public Fields() {
		_content = new LinkedHashMap<String, String>();
	}

	public static Fields empty() {
		return new Fields();
	}

	public static Fields fromRaw(String someSerializedFields) {
		Fields result = new Fields();
		if(!someSerializedFields.contains("|")){
			result.putAll(retrieveField(someSerializedFields));
		}else{
			for (String each: someSerializedFields.split("\\|")) {
				result.putAll(retrieveField(each));
			};
		}
		return result;
	}

	public Fields putAll(Fields someFields) {
		return putAll(someFields, false);
	}
	
	public Fields putAll(Fields someFields, Boolean forceOverride) {
		if(someFields == null) return this;
		for (String eachKey : someFields.keys()) {
			put(eachKey, someFields.firstValueFor(eachKey), forceOverride);
		}
		return this;
	}

	public boolean containsKey(String aKey) {
		return _content.keySet().contains(aKey);
	}

	public List<String> keys() {
		return new ArrayList<String>(_content.keySet());
	}

	private static Fields retrieveField(String someSerializedFields) {
		Fields result = Fields.empty();
		if (someSerializedFields.contains("=")) {
			String[] content = someSerializedFields.split("=");
			if(content.length<2) return result;
			result.put(content[0], content[1].replace("%e", "="));
			return result;
		}
		return Fields.empty();
	}

	public String toRaw() {
		StringBuffer result = new StringBuffer();
		for (String each : _content.keySet()) {
			result.append(each + "=" + _content.get(each) + "|");
		}
		return StringUtils.removeLastChar(result.toString());
	}

	public Fields put(String aKey, String aValue) {
		return put(aKey, aValue, false);
	}
	
	// TODO: scenario dove una variabile si chiama come l'inizio di un altra.
	public Fields put(String aKey, String aValue, Boolean forceOverride) {
		if(aKey == null) return this;
		if(aValue == null) return this;
		if(aValue.isEmpty()) return this;
		if(forceOverride){
			_content.put(aKey, aValue);
			return this;
		}
		if(containsKeyPrefix(aKey)){
			int suffix = retrieveLastSuffix(aKey);
			if(suffix == 1){
				_content.put(aKey + "."+ suffix, firstValueFor(aKey));
				_content.remove(aKey);
			}
			suffix++;
			_content.put(aKey + "."+ suffix, aValue);
		}else{
			_content.put(aKey, aValue);
		}
		return this;
	}

	private boolean containsKeyPrefix(String aKey) {
		for (String each : keys()) {
			if(each.startsWith(aKey)) return true;
		}
		return false;
	}
	
	private int retrieveLastSuffix(String aKey) {
		List<String> currentList = allKeysStartingWith(aKey);
		return Integer.valueOf(currentList.size());
	}

	public List<String> allKeysStartingWith(String aKey) {
		ArrayList<String> result = new ArrayList<String>();
		for (String each : _content.keySet()) {
			if(each.startsWith(aKey)) result.add(each);
		}
		return result;
	}

	public String firstValueFor(String aKey) {
		return firstValueFor(aKey, "");
	}
	
	public String firstValueFor(String aKey, String defaultString) {
		if(!_content.keySet().contains(aKey)) return defaultString;
		String value = _content.get(aKey);
		return value;
	}

	public Fields selectPrefix(String aKey) {
		Fields result = Fields.empty();
		int size = allKeysStartingWith(aKey).size();
		if(size == 0) return Fields.empty();
		result.putAll(select(allKeysStartingWith(aKey).toArray(new String[size - 1])));
		return result;
	}

	public Fields select(String...allKeysStartWith) {
		Fields result = Fields.empty();
		for (String each : allKeysStartWith) {
			if(!containsKey(each)) continue;
			result.put(each, firstValueFor(each));
		}
		return result;
	}

	public static Fields single(String aKey, String aValue) {
		return empty().put(aKey, aValue);
	}

	public Fields remove(String aKey) {
		_content.remove(aKey);
		return this;
	}

	public boolean containsAll(Fields someFields) {
		for (String each : someFields.keys()) {
			if(!containsKey(each)) return false;
			if(!firstValueFor(each).equals(someFields.firstValueFor(each))) return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return _content.isEmpty();
	}

	public boolean isSubSet(Fields someFields) {
		if(keys().size() == 0) return true;
		for (String each : keys()) {
			if(!someFields.containsKey(each)) return false;
			if(!someFields.firstValueFor(each).equals(firstValueFor(each))) return false;
		}
		return true;
	}
	
	public boolean equals(Fields someFields){
		if(someFields.keys().size() != keys().size()){
			System.out.println("DIFFERENT SIZE "+ someFields.keys().size() + " - "+ keys().size());
			return false;
		}
		for (String each : _content.keySet()) {
			if(!someFields.containsKey(each)) {
				System.out.println("DIFFERENT KEYS "+ !someFields.containsKey(each));
				return false;
			}
			if(!someFields.firstValueFor(each).equals(firstValueFor(each))) {
				System.out.println(""+ someFields.firstValueFor(each) + " is different "+ firstValueFor(each));
				return false;
			}
		}
		return true;
	}

	public Fields renameKey(String oldKey, String newKey) {
		String value = _content.get(oldKey);
		_content.remove(oldKey);
		put(newKey, value);
		return this;
	}

	public boolean keyAsBoolean(String aKey) {
		return Boolean.valueOf(firstValueFor(aKey));
	}

	public Fields removePrefix(String aPrefix) {
		for (String each : keys()) {
			if(each.startsWith(aPrefix)){
				renameKey(each, each.replaceAll(aPrefix + ".", ""));
			}
		}
		return this;
	}

	public Fields copy() {
		Fields result = Fields.empty();
		for (String each : _content.keySet()) {
			result.put(each, firstValueFor(each));
		}
		return result;
	}

	public void clear() {
		_content.clear();
	}

	public Fields rejectKeysStartingWith(String keyStartWith) {
		for (String each : allKeysStartingWith(keyStartWith)) {
			remove(each);
		};
		return this;
	}

	public Fields applyPrefix(String aPrefix) {
		for (String eachKey : new ArrayList<String>(_content.keySet())) {
			renameKey(eachKey, aPrefix + "."+ eachKey);
		}
		return this;
	}

	public Map<String, String> asMap() {
		return _content;
	}
}

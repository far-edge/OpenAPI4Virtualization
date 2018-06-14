package ch.supsi.isteps.prototype.commons;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractJsonParsingLogicStrategy {

	public abstract void parse(Map<String, String> someFields, Iterator<Entry<String, JsonNode>> currentElement);

}

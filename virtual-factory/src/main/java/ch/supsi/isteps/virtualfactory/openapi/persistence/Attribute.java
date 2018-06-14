package ch.supsi.isteps.virtualfactory.openapi.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.supsi.isteps.virtualfactory.tools.Fields;

@Entity
@Table(name = "attributes")
public class Attribute {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "elementName")
	private String elementName;
	
	@Column(name = "elementId")
	private String elementId;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

	Attribute() {
	}

	public Attribute(String elementId, String elementName, String key, String value) {
		this.elementName = elementName;
		this.elementId = elementId;
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("Attribute[id=%d, elementName='%s', key='%s', value='%s']", id, elementName, key, value);
	}

	public Fields toFields() {
		return Fields.single(key, value);
	}
}
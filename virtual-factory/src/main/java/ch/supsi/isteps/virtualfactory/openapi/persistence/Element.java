package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;

@Entity
@Table(name = "elements")
@IdClass(Element.class)
public class Element implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Id
	@Column(name = "elementName")
	private String elementName;
	
	@Column(name = "layerName")
	private String layerName;
 
	@Column(name = "archetypeName")
	private String archetypeName;
	
	
 
	Element() {
	}
 
	public Element(String layerName, String elementName, String archetypeName) {
		this.layerName = layerName;
		this.elementName = elementName;
		this.archetypeName = archetypeName;
	}
 
	@Override
	public String toString() {
		return String.format("Element[id=%d, layerName='%s', named='%s']", id, layerName, elementName);
	}

	public String name() {
		return elementName;
	}
	public String id() {
		return String.valueOf(id);
	}
	
	
	//id=125|layerName=LogicalLayer|archetype=Conveyor|elementName=Conveyor1
	public Fields toFields() {
		Fields result = new Fields();
		result.put(RealToDigitalSyncData.ID, Long.toString(id));
		result.put(RealToDigitalSyncData.LAYER_NAME, layerName);
		result.put(RealToDigitalSyncData.ARCHETYPE, archetypeName);
		result.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
		return result;
	}
	
	
	
}
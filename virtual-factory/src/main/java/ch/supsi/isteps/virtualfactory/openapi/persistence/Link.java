package ch.supsi.isteps.virtualfactory.openapi.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;

@Entity
@Table(name="links")
public class Link {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "fromElement")
	private String fromElement;
	
	@Column(name = "toElement")
	private String toElement;

	@Column(name = "linkType")
	private String linkType;
	
 
	Link() {
	}
 
	public Link(String fromElement, String toElement, String linkType) {
		this.fromElement = fromElement;
		this.toElement = toElement;
		this.linkType = linkType;
	}
 
	@Override
	public String toString() {
		return String.format("Link[id=%d, fromElement='%s', toElement='%s', linkType='%s']", id, fromElement, toElement, linkType);
	}

	public String fromElement() {
		return fromElement;
	}
	public String toElement() {
		return toElement;
	}
	public String linkType() {
		return linkType;
	}
	
	
	//fromElement=Sorter|toElement=Plant|linkType=isContained
	public Fields toFields() {
		Fields result = new Fields();
		result.put(RealToDigitalSyncData.ID, Long.toString(id));
		result.put(RealToDigitalSyncData.FROM_ELEMENT, fromElement);
		result.put(RealToDigitalSyncData.TO_ELEMENT, toElement);
		result.put(RealToDigitalSyncData.LINK_TYPE, linkType);
		return result;
	}
	
	
	
}

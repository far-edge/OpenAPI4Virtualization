package ch.supsi.isteps.virtualfactory.controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Elements")
public class Element {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "layerName")
	private String layerName;
 
	@Column(name = "named")
	private String named;
 
	Element() {
	}
 
	public Element(String layerName, String named) {
		this.layerName = layerName;
		this.named = named;
	}
 
	@Override
	public String toString() {
		return String.format("Element[id=%d, layerName='%s', named='%s']", id, layerName, named);
	}

	public String name() {
		return named;
	}

}
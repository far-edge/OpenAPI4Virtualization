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
@Table(name = "layers")
public class Layer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "layerName")
	private String layerName;
	
	@Column(name = "layerDescription")
	private String layerDescription;
	
	Layer() {
	}
 
	public Layer(String layerName, String layerDescription) {
		this.layerName = layerName;
		this.layerDescription = layerDescription;
	}
 
	@Override
	public String toString() {
		return String.format("Element[id=%d, layerName='%s', layerDescription='%s']", id, layerName, layerDescription);
	}

	public String name() {
		return layerName;
	}
	public String description() {
		return layerDescription;
	}
	
	//layerName=LogicalLayer|layerDescription=Warehouse
	public Fields toFields() {
		Fields result = new Fields();
		result.put(RealToDigitalSyncData.LAYER_NAME, layerName);
		result.put(RealToDigitalSyncData.LAYER_DESCRIPTION, layerDescription);
		return result;
	}
}

package ch.supsi.isteps.virtualfactory.controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.supsi.isteps.virtualfactory.tools.Fields;

@Entity
@Table(name = "attributes")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "filePath")
	private String filePath;

	Asset() {
	}
	
	public Asset(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return String.format("Asset[id=%d, filePath='%s']", id, filePath);
	}

	public void copyIdIn(Fields result) {
		result.put("id", String.valueOf(id));
	}
}

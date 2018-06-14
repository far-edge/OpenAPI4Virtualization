package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;

@Entity
@Table(name = "assets")
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

	public Fields toFields() {
		System.out.println("Extracting filepath and filename: " + filePath);
		// Path p = Paths.
		// String directory = p.getParent().toString();
		// String fileName = p.getFileName().toString();
		String fileName = "";
		String directory = "";
		fileName = FilenameUtils.getName(filePath);
		directory = FilenameUtils.getFullPath(filePath);

		Fields result = Fields.empty();
		result.put(RealToDigitalSyncData.ASSET_FILE_NAME, fileName);
		result.put(RealToDigitalSyncData.ASSET_FILE_PATH, directory);
		return result;
	}
}

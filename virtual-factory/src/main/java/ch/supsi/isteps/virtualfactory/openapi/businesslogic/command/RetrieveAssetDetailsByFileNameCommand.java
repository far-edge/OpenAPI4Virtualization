package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import java.util.Map;

import ch.supsi.isteps.prototype.tools.AbstractComponentFactory;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.InitializeJarFile;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class RetrieveAssetDetailsByFileNameCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public RetrieveAssetDetailsByFileNameCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields assetFileName = _repository.retrieveAssetByFileName(anInput.select(RealToDigitalSyncData.ASSET_FILE_NAME));
		String filePath = assetFileName.firstValueFor(RealToDigitalSyncData.ASSET_FILE_PATH);
		AbstractComponentFactory factory = InitializeJarFile.createFactory(filePath);
		Fields result = Fields.single("outcome", "true");
		Map<String, String> currentMap = factory.descriptionAsMap();
		for (String eachKey: currentMap.keySet()) {
			result.put(eachKey, currentMap.get(eachKey));
		}
		anOutput.putAll(result);
	}
}
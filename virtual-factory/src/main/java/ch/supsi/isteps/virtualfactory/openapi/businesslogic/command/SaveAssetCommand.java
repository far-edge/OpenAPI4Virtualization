package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class SaveAssetCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public SaveAssetCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		anOutput.putAll(_repository.saveAsset(anInput.select(RealToDigitalSyncData.ASSET_FILE_NAME, RealToDigitalSyncData.ASSET_FILE_PATH)));
	}
}
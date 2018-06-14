package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class RetrieveAssetByFileNameCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public RetrieveAssetByFileNameCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields result = Fields.empty();
		result.put(ToolData.OUTCOME, "true");
		result.putAll(_repository.retrieveAssetByFileName(anInput.select(RealToDigitalSyncData.ASSET_FILE_NAME)));
		anOutput.putAll(result);
	}
}
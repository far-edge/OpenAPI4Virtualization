package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class RetrieveLinkByIdCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public RetrieveLinkByIdCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields result = _repository.retrieveLinkById(anInput.select(RealToDigitalSyncData.ID));
		if(result.keyAsBoolean(ToolData.OUTCOME)) {
			anOutput.put(ToolData.OUTCOME, "true");
			anOutput.putAll(result.rejectKeysStartingWith(ToolData.OUTCOME));
		}else {
			anOutput.put(ToolData.OUTCOME, "false");
		}
	}
}
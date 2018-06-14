package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class SaveElementCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public SaveElementCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields answer = _repository.retrieveElementByName(anInput.select(RealToDigitalSyncData.ELEMENT_NAME));
		if(!answer.rejectKeysStartingWith(ToolData.OUTCOME).isEmpty()) return;
		Fields result = _repository.saveElement(anInput.select(RealToDigitalSyncData.LAYER_NAME, RealToDigitalSyncData.ARCHETYPE, RealToDigitalSyncData.ELEMENT_NAME));
		if(result.keyAsBoolean(ToolData.OUTCOME)) {
			anOutput.put(ToolData.OUTCOME, "true");
			anOutput.putAll(result.select(RealToDigitalSyncData.ID));
		}else {
			anOutput.put(ToolData.OUTCOME, "false");
		}
	}
}
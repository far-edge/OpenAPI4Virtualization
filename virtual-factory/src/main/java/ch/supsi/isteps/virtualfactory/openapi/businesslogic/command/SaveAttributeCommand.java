package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class SaveAttributeCommand extends AbstractCommand {


	private AbstractPersistence _repository;

	public SaveAttributeCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields answer = _repository.retrieveAllAttributesByElement(anInput.select(RealToDigitalSyncData.ELEMENT_NAME, RealToDigitalSyncData.LAYER_NAME));
		//in case the key is already present return to avoid duplications of the same attribute
		if(answer.containsKey(anInput.firstValueFor(RealToDigitalSyncData.KEY))) {
			System.out.println("KEY " + anInput.select(RealToDigitalSyncData.KEY).toRaw() + " ALREADY PRESENT IN THE LIST OF ATTRIBUTES.");
			return;
		}
		
		//Fields answer = _repository.retrieveAttributeByKey(anInput.select(RealToDigitalSyncData.KEY));
		//if(!answer.rejectKeysStartingWith(ToolData.OUTCOME).isEmpty()) return;
		Fields result = _repository.saveAttribute(anInput.select(RealToDigitalSyncData.ELEMENT_NAME, RealToDigitalSyncData.LAYER_NAME, RealToDigitalSyncData.KEY, RealToDigitalSyncData.VALUE));
		if(result.keyAsBoolean(ToolData.OUTCOME)) {
			anOutput.put(ToolData.OUTCOME, "true");
			anOutput.putAll(result.select(RealToDigitalSyncData.ID));
		}else {
			anOutput.put(ToolData.OUTCOME, "false");
		}
	}
}
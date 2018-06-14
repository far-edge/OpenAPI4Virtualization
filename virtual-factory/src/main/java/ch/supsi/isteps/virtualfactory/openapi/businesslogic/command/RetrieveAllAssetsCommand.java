package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class RetrieveAllAssetsCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public RetrieveAllAssetsCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields allAssets = _repository.retrieveAllAssets();
		anOutput.put(ToolData.OUTCOME, "true");
		anOutput.putAll(allAssets);
	}
}
package ch.supsi.isteps.virtualfactory.openapi.businesslogic.command;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class ClearRepositoryCommand extends AbstractCommand {

	private AbstractPersistence _repository;

	public ClearRepositoryCommand(AbstractPersistence repository) {
		_repository = repository;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_repository.clear();
	}
}
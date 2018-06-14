package ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class RealToDigitalSynchronizationCommand extends AbstractCommand {

	private AbstractCommand _syntaxValidationCommand;
	private AbstractCommand _semanticValidationCommand;
	private AbstractCommand _dataTranslationCommand;
	private AbstractCommand _modelUpdaterCommand;

	public RealToDigitalSynchronizationCommand(
			AbstractCommand syntaxValidationCommand, AbstractCommand semanticValidationCommand,
			AbstractCommand dataTranslationCommand, AbstractCommand modelUpdaterCommand) {
				_syntaxValidationCommand = syntaxValidationCommand;
				_semanticValidationCommand = semanticValidationCommand;
				_dataTranslationCommand = dataTranslationCommand;
				_modelUpdaterCommand = modelUpdaterCommand;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Fields syntaxValidationInput = Fields.empty().putAll(anInput.select(RealToDigitalSyncData.SENSOR_NAME, RealToDigitalSyncData.MESSAGE));
		Fields syntaxValidationOutput = Fields.empty();
		_syntaxValidationCommand.execute(syntaxValidationInput, syntaxValidationOutput);
		
		Fields semanticValidationInput = Fields.empty().putAll(anInput.select(RealToDigitalSyncData.MESSAGE));
		Fields semanticValidationOutput = Fields.empty();
		_semanticValidationCommand.execute(semanticValidationInput, semanticValidationOutput);
		
		Fields dataTranslationInput = Fields.empty().putAll(anInput.select(RealToDigitalSyncData.MESSAGE));
		Fields dataTranslationOutput = Fields.empty();
		_dataTranslationCommand.execute(dataTranslationInput, dataTranslationOutput);
		
		Fields modelUpdaterInput = dataTranslationOutput;
		Fields modelUpdaterOutput = Fields.empty();
		_modelUpdaterCommand.execute(modelUpdaterInput, modelUpdaterOutput);
	}
}
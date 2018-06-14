package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.Map;

import ch.supsi.isteps.prototype.commons.AbstractPersonalizedStep;
import ch.supsi.isteps.prototype.data.StepData;
import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command.LogIOCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.command.StepToCommandAdapterCommand;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.platform.command.RealToDigitalSynchronizationCommand;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class ProductiveConnectionFactory extends AbstractConnectionFactory {

	private AbstractOpenAPIForVirtualization _openApiForVirtualization;

	public ProductiveConnectionFactory(AbstractOpenAPIForVirtualization openApiForVirtualization) {
		_openApiForVirtualization = openApiForVirtualization;
	}

	@Override
	public XSystem createUsing(Fields someFields) {
		Map<String, AbstractPersonalizedStep> workflowSteps = InitializeJarFile.createFactory(someFields.firstValueFor(RealToDigitalSyncData.RESOURCE_PATH)).create(_openApiForVirtualization);
		AbstractCommand syntaxValidationCommand = new LogIOCommand("SyntaxValidation", new StepToCommandAdapterCommand(workflowSteps.get(StepData.SYNTAX_VALIDATION)));
		AbstractCommand semanticValidationCommand = new LogIOCommand("Semantic Validation", new StepToCommandAdapterCommand(workflowSteps.get(StepData.SEMANTIC_VALIDATION)));
		AbstractCommand dataTranslationCommand = new LogIOCommand("Data Translation", new StepToCommandAdapterCommand(workflowSteps.get(StepData.DATA_TRANSLATION)));
		AbstractCommand modelUpdaterCommand = new LogIOCommand("Model Updater", new StepToCommandAdapterCommand(workflowSteps.get(StepData.MODEL_UPDATER)));

		CommandHandler commandHandler = new CommandHandler();
		commandHandler.put(RealToDigitalSyncCommandData.RUN, new RealToDigitalSynchronizationCommand(syntaxValidationCommand, semanticValidationCommand, dataTranslationCommand, modelUpdaterCommand));
		return new XSystem("Real To Digital Synchronization", commandHandler);
	}
}
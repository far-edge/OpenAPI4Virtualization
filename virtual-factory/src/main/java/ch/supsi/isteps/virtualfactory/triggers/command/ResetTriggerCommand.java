package ch.supsi.isteps.virtualfactory.triggers.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class ResetTriggerCommand extends AbstractCommand {

	private XSystem _dataModel;
	private XSystem _originalDataModel;
	private AbstractCommand _applyTriggerCommand;
	private XMap<Fields> _triggers;

	public ResetTriggerCommand(XMap<Fields> triggers, ApplyTriggerCommand applyTriggerCommand, XSystem dataModel, XSystem originalDataModel) {
		_triggers = triggers;
		_applyTriggerCommand = applyTriggerCommand;
		_dataModel = dataModel;
		_originalDataModel = originalDataModel;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		_dataModel.replaceWith(_originalDataModel);
		_triggers.remove(anInput.firstValueFor("called"));
		for (String each : _triggers.keys()) {
			_applyTriggerCommand.execute(Fields.single("called", each), Fields.empty());
		}
	}
}
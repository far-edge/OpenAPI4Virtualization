package ch.supsi.isteps.virtualfactory.triggers.command;

import java.util.List;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class CallbackCommand extends AbstractCommand {

	private AbstractCommand _command;
	private List<String> _keys;
	private XSystem _proSystem;
	private Fields _inputFields;

	public CallbackCommand(List<String> keys, Fields inputFields, XSystem currentSystem, AbstractCommand command) {
		_keys = keys;
		_inputFields = inputFields;
		_proSystem = currentSystem;
		_command = command;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		boolean run = false;
		for (String each : _keys) {
			if(!_inputFields.containsKey(each)) {
				run = false;
				break;
			}
			if(_inputFields.containsKey(each)) {
				run = true;
			}
		}
		if(run) {
			_proSystem.execute(anInput, anOutput);
		}
		_command.execute(anInput, anOutput);
	}
}
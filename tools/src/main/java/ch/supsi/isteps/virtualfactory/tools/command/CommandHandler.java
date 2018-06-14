package ch.supsi.isteps.virtualfactory.tools.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class CommandHandler {

	private LinkedHashMap<String, AbstractCommand> _commands;
	private AbstractCommand _defaultCommand;
	
	public CommandHandler(AbstractCommand aCommand) {
		_defaultCommand = aCommand;
		_commands = new LinkedHashMap<String, AbstractCommand>();
	}

	public CommandHandler() {
		this(new UnknownCommand("message"));
	}

	public List<String> keys() {
		return new ArrayList<String>(_commands.keySet());
	}

	public void put(String aName, AbstractCommand aCommand) {
		_commands.put(aName, aCommand);
	}

	public AbstractCommand named(String aCommandName) {
		if(!_commands.containsKey(aCommandName)) return _defaultCommand;
		return _commands.get(aCommandName);
	}

	public void clear() {
		_commands.clear();
	}

	public void putAll(CommandHandler commandHandler) {
		_commands.putAll(commandHandler._commands);
	}

	public CommandHandler copy() {
		CommandHandler result = new CommandHandler();
		result.putAll(this);
		return result;
	}
}
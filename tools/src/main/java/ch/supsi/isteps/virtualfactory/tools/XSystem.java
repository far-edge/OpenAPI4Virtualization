package ch.supsi.isteps.virtualfactory.tools;

import java.util.List;

import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;
import ch.supsi.isteps.virtualfactory.tools.logger.PrintStreamLogger;

public class XSystem {

	private CommandHandler _commandHandler;
	private String _name;
	private PrintStreamLogger _logger;

	public XSystem(CommandHandler commandHandler) {
		this("no name", commandHandler);
	}
	
	public XSystem(String name, CommandHandler commandHandler) {
		_name = name;
		_commandHandler = commandHandler;
		_logger = new PrintStreamLogger(System.out);
	}

	public List<String> commandList() {
		return _commandHandler.keys();
	}

	public void execute(Fields anInput, Fields anOutput) {
		String commandName = anInput.firstValueFor("commandName");
		_logger.log("==========================");
		_logger.log("System Name: " + _name);
		_logger.log(">>>> "+ anInput.toRaw());
		_commandHandler.named(commandName).execute(anInput, anOutput);
		_logger.log("<<<< "+ anOutput.toRaw());
		_logger.log("-----------------------------------------------");
	}

	public static XSystem single(AbstractCommand aCommand) {
		CommandHandler commandHandler = new CommandHandler(aCommand);
		return new XSystem(commandHandler);
	}

	public CommandHandler commandHandler() {
		return _commandHandler;
	}

	public void help(Fields output) {
		StringBuffer result = new StringBuffer();
		result.append("AVAILABLE COMMANDS:\n");
		for (String each : _commandHandler.keys()) {
			result.append(each + "\n");
		}
		output.put("message", result.toString());
	}

	public void replaceWith(XSystem aSystem) {
		CommandHandler commandHandler = aSystem.commandHandler().copy();
		_commandHandler.clear();
		_commandHandler.putAll(commandHandler);
	}
}
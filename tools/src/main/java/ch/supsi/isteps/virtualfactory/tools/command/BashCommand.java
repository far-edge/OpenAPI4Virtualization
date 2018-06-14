package ch.supsi.isteps.virtualfactory.tools.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.logger.AbstractLogger;
import ch.supsi.isteps.virtualfactory.tools.logger.PrintStreamLogger;

public class BashCommand extends AbstractCommand {

	private String _defaultBashCommand;
	private AbstractLogger _logger;

	public BashCommand() {
		this("");
	}
	
	public BashCommand(String defaultBashCommand) {
		_defaultBashCommand = defaultBashCommand;
		_logger = new PrintStreamLogger(System.out);
//		_logger = new NoLogger();
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		String command = _defaultBashCommand;
		if (anInput.containsKey("commandLine")){
			command =  anInput.firstValueFor("commandLine");
		}
		String[] cmd = { "/bin/bash", "-c", command };
		execCommand(cmd);
	}
	
	public void execCommand(String[] cmd) {
		String line;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = stdoutReader.readLine()) != null) {
				_logger.log("Output: "+ line);
			}
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = stderrReader.readLine()) != null) {
				_logger.log("Error: "+ line);
			}
			int retValue = p.waitFor();
			_logger.log("Return value "+ retValue);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
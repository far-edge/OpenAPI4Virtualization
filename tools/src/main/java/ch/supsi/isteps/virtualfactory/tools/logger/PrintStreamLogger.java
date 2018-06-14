package ch.supsi.isteps.virtualfactory.tools.logger;

import java.io.PrintStream;

public class PrintStreamLogger extends AbstractLogger{

	private PrintStream _out;

	public PrintStreamLogger(PrintStream out) {
		_out = out;
	}

	@Override
	public void log(String aLog) {
		_out.println(aLog);
	}
}

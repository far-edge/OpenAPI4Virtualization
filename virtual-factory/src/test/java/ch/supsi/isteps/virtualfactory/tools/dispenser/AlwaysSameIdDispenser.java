package ch.supsi.isteps.virtualfactory.tools.dispenser;

public class AlwaysSameIdDispenser extends AbstractDispenser{

	private String _value;

	public AlwaysSameIdDispenser(String value) {
		_value = value;
	}

	@Override
	public String next() {
		return _value;
	}
}
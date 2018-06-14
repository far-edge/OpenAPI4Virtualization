package ch.supsi.isteps.virtualfactory.tools.dispenser;

public class IncrementalDispenser extends AbstractDispenser {

	private int _startValue;

	public IncrementalDispenser(int startValue) {
		_startValue = startValue;
	}

	@Override
	public String next() {
		_startValue = _startValue + 1;
		return String.valueOf(_startValue);
	}

}

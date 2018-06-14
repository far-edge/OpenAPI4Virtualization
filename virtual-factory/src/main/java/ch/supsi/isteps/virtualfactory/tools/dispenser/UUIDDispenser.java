package ch.supsi.isteps.virtualfactory.tools.dispenser;

import java.util.UUID;

public class UUIDDispenser extends AbstractDispenser{

	
	@Override
	public String next() {
		return UUID.randomUUID().toString();
	}
}
package ch.supsi.isteps.virtualfactory.triggers.command;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.triggers.RemoteProxyFactory;

public class RegisterTriggerCommand extends AbstractCommand {

	private XMap<Fields> _triggers;
	private XMap<XSystem> _proxies;
	private RemoteProxyFactory _proxyFactory;

	public RegisterTriggerCommand(RemoteProxyFactory proxyFactory, XMap<XSystem> proxies, XMap<Fields> triggers) {
		_proxyFactory = proxyFactory;
		_proxies = proxies;
		_triggers = triggers;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		String name = anInput.firstValueFor("called");
		String callback = anInput.firstValueFor("callback");
		_proxies.put(callback, _proxyFactory.create(callback));
		_triggers.put(name, anInput.select("checkKeys", "onEvent", "callback"));
		anOutput.put("outcome", "true").put("message", "trigger registered");
	}
}
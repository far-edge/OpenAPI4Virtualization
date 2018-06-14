package ch.supsi.isteps.virtualfactory.triggers;

import org.springframework.web.client.RestTemplate;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class RemoteProxyFactory {

	private XSystem _default;

	public RemoteProxyFactory() {
	}
	
	public RemoteProxyFactory(XSystem defaultSystem) {
		_default = defaultSystem;
	}
	
	public XSystem create(String aUrl) {
		if(_default != null) return _default;
		RestTemplate restTemplate = new RestTemplate();
		return XSystem.single(new AbstractCommand() {
			@Override
			public void execute(Fields anInput, Fields anOutput) {
				String result = restTemplate.getForObject(aUrl, String.class);
				anOutput.put("message", result);
			}
		});
	}
}
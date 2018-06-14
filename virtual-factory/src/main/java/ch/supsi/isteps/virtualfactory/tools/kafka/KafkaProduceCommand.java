package ch.supsi.isteps.virtualfactory.tools.kafka;


import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class KafkaProduceCommand extends AbstractCommand {

	private Fields _currentConfiguration;

	public KafkaProduceCommand(Fields currentConfiguration) {
		_currentConfiguration = currentConfiguration;
	}

	@Override
	public void execute(Fields anInput, Fields anOutput) {
		try {
			String uri = _currentConfiguration.firstValueFor("uri");
			KafkaChannel channel = new KafkaChannel();
			channel.connect("kafka:9092");
			String topic = anInput.firstValueFor("topic");
			String message = anInput.firstValueFor("message");
			channel.send(topic, message);
			channel.disconnect();
		} catch (Exception e) {
			anOutput.put("outcome", "false").put("message", e.getMessage());
			return;
		}
		anOutput.put("outcome", "true");
	}
}

package ch.supsi.isteps.virtualfactory.tools.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;

public class KafkaConsumeCommand extends AbstractCommand {

	private Fields _currentConfiguration;

	public KafkaConsumeCommand(Fields currentConfiguration) {
		_currentConfiguration = currentConfiguration;
	}
	
	@Override
	public void execute(Fields anInput, Fields anOutput) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", _currentConfiguration.firstValueFor("uri"));
		properties.put("group.id", "1");
		properties.put("key.deserializer", StringDeserializer.class.getName());
		properties.put("value.deserializer", StringDeserializer.class.getName());
		properties.put("flush.ms", "1");
		KafkaConsumer consumer = new KafkaConsumer(properties);
		List<String> topics = new ArrayList<String>();
		topics.add(anInput.firstValueFor("topic"));
		consumer.subscribe(topics);
		ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
		anOutput.put("outcome", "true");
		for (ConsumerRecord<String, String> record : records) {
			String topic = record.topic();
			anOutput.put(topic, record.value());
		}
	}
}
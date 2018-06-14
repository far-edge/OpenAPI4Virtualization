package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.data.ConstantData;

public class ProductiveKafkaClient extends AbstractFaredgeApiClient {

	private static KafkaConsumer<String, String> _consumer;
	private Thread _pollingThread;

	public ProductiveKafkaClient(Fields configuration) {
		Properties props = new Properties();
		props.put("bootstrap.servers", configuration.firstValueFor(ConstantData.HOST));
		props.put("group.id", "1");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		_consumer = new KafkaConsumer<String, String>(props);
	}

	@Override
	public List<String> retrieveActiveSensors() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("BOL");
		result.add("EOLGR");
		result.add("EOLST");
		return result;
	}

	@Override
	public void subscribe(String sensorName, final AbstractEventHandler eventHandler) {
		List<String> topics = new ArrayList<String>();
		topics.add(sensorName);
		synchronized (_consumer) {
			if(_consumer.listTopics().containsKey(sensorName)) {
				_consumer.subscribe(topics);
			}
		}
		if (_pollingThread == null) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					boolean run = true;
					while (run) {
						run = _pollingThread.isInterrupted();
						synchronized (_consumer) {
							ConsumerRecords<String, String> records = _consumer.poll(Long.MAX_VALUE);
							for (ConsumerRecord<String, String> record : records) {
								eventHandler.notify(record.topic(), record.value());
							}
							_consumer.commitSync();
						}
					}
				}
			};
			_pollingThread = new Thread(runnable);
			_pollingThread.start();
		}
	}

	@Override
	public void unscribe(String name) {
		synchronized (_consumer) {
			if(_consumer.listTopics().keySet().contains(name)) {
				_consumer.unsubscribe();
				try {
					if(_pollingThread != null) {
						_pollingThread.interrupt();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				_pollingThread = null;
			}
		}
	}
}

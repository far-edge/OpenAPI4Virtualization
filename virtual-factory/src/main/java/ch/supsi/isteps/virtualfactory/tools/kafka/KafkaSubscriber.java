package ch.supsi.isteps.virtualfactory.tools.kafka;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaSubscriber {

	private KafkaConsumer _consumer;
	private Thread _listener;

	public KafkaSubscriber(KafkaConsumer consumer, Thread listener) {
		_consumer = consumer;
		_listener = listener;
	}

	public synchronized void subscribe(String topic) {
		synchronized (_consumer) {
			_consumer.subscribe(Arrays.asList(topic));
		}
		_listener.start();
	}

	public synchronized void unsubscribe() {
		_listener.interrupt();
		_consumer.unsubscribe();
	}
}

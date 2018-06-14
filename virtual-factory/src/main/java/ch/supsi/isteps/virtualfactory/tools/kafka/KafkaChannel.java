package ch.supsi.isteps.virtualfactory.tools.kafka;


import java.io.Serializable;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaChannel implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient org.apache.kafka.clients.producer.KafkaProducer _producer = null;

	public KafkaChannel() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
	}

	public synchronized void connect(String uri) {
		if (_producer != null) {
			disconnect();
		}
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, uri);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//		properties.put(ProducerConfig.ACKS_CONFIG, "1");
//		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "10");
//		properties.put(ProducerConfig.SEND_BUFFER_CONFIG, "10");
		_producer = new org.apache.kafka.clients.producer.KafkaProducer(properties);
	}

	public synchronized void disconnect() {
		if (_producer != null) {
			_producer.close();
			_producer = null;
		}
	}

	public synchronized void send(String topic, String message) {
		_producer.send(new ProducerRecord(topic, message));
		_producer.flush();
	}
}

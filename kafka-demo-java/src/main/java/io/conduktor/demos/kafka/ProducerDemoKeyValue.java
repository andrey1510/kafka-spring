package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeyValue {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeyValue.class);

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        String bootstrapServers = "localhost:29092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i=0; i<3; i++ ) {

            // create a producer record

            String topic = "demo_javca2";
            String value = "heello world " + i;
            String key = "id_" + i;

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic, key, value);

            // send data - asynchronous
            producer.send(producerRecord, (recordMetadata, e) -> {
                // executes every time a record is successfully sent or an exception is thrown
                if (e == null) {
                    // the record was successfully sent
                    log.info("Received new metadata. \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Key:" + producerRecord.key() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    log.error("Error while producing", e);
                }
            });
        }
        // flush data - synchronous
        producer.flush();
        // flush and close producer
        producer.close();
    }
}

package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class KafkaTestConsumer {

    private final KafkaConsumer<String, String> consumer;

    public KafkaTestConsumer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
    }

    public List<String> readMessages(String topic, int timeoutSeconds, int expectedCount) {
        consumer.subscribe(Collections.singletonList(topic));

        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        List<String> messages = new ArrayList<>();

        while (System.currentTimeMillis() < endTime && messages.size() < expectedCount) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());

                if (messages.size() == expectedCount) {
                    return messages;
                }
            }
        }

        throw new RuntimeException(
                "Ожидали " + expectedCount + " сообщений, а получили " + messages.size()
        );
    }

    public void close() {
        consumer.close();
    }
}
package kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class KafkaSmokeTest {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(KafkaSmokeTest.class);

    @Test
    public void sendAndGetMessages(){
        String bootstrapServers = "localhost:9092";
        String topic = "test-topic";
        String expectedMessage1 = "Hello from test1";
        String expectedMessage2 = "Hello from test2";
        String expectedMessage3 = "Hello from test3";
        KafkaTestProducer kafkaTestProducer = new KafkaTestProducer(bootstrapServers);
        KafkaTestConsumer kafkaTestConsumer = new KafkaTestConsumer(bootstrapServers);
        kafkaTestProducer.send(topic, expectedMessage1);
        kafkaTestProducer.send(topic, expectedMessage2);
        kafkaTestProducer.send(topic, expectedMessage3);
        List<String> messages = new ArrayList<>();
        messages.add(expectedMessage1);
        messages.add(expectedMessage2);
        messages.add(expectedMessage3);
        List<String> actualMessages = kafkaTestConsumer.readMessages(topic,10,3);
        for (String string:actualMessages){
            System.out.println(string);
        }
        //stream, поиск uuid
        System.out.println(actualMessages.get(1));
        System.out.println("Message send sout");
        System.out.println(topic);
        log.info("Message send");
        Assertions.assertEquals(messages, actualMessages);
        log.info("Message get");
        kafkaTestProducer.close();
        kafkaTestConsumer.close();
    }
}

package demo.lib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClient {

    private static final String DEFAULT_TOPIC = "demo-topic";

    @Autowired
    private final KafkaTemplate kafkaTemplate;

    /**
     * Sends events asynchronously - does not wait for success acknowledgement.
     */
    public void sendMessage(String payload) {
        try {
            final ProducerRecord<String, String> record = new ProducerRecord<>(DEFAULT_TOPIC, payload);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            String message = "Error sending message to topic.";
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }
}

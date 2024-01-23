package demo.lib;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaClientTest {

    private KafkaTemplate kafkaTemplateMock;
    private KafkaClient kafkaClient;

    @BeforeEach
    public void setUp() {
        kafkaTemplateMock = mock(KafkaTemplate.class);
        kafkaClient = new KafkaClient(kafkaTemplateMock);
    }

    /**
     * Ensure the Kafka client is called to emit a message.
     */
    @Test
    public void testProcess_Success() throws Exception {
        String data = randomUUID().toString();

        final ProducerRecord<String, String> expectedRecord = new ProducerRecord<>("demo-topic", data);
        CompletableFuture futureResult = mock(CompletableFuture.class);
        when(kafkaTemplateMock.send(any(ProducerRecord.class))).thenReturn(futureResult);

        kafkaClient.sendMessage(data);

        verify(kafkaTemplateMock, times(1)).send(expectedRecord);
    }

    /**
     * Ensure that an exception thrown on the send is cleanly handled.
     */
    @Test
    public void testProcess_ExceptionOnSend() throws Exception {
        String data = randomUUID().toString();

        final ProducerRecord<String, String> expectedRecord = new ProducerRecord<>("demo-topic", data);

        doThrow(new RuntimeException("Kafka send failure", new Exception("Failed"))).when(kafkaTemplateMock).send(any(ProducerRecord.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
                kafkaClient.sendMessage(data);
        });

        verify(kafkaTemplateMock, times(1)).send(expectedRecord);
        assertThat(exception.getMessage(), equalTo("Error sending message to topic."));
    }
}

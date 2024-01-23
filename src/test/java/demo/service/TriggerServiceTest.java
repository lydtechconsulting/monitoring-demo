package demo.service;

import demo.lib.KafkaClient;
import demo.rest.api.TriggerEventsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TriggerServiceTest {

    private KafkaClient mockKafkaClient;
    private TriggerService service;

    @BeforeEach
    public void setUp() {
        mockKafkaClient = mock(KafkaClient.class);
        service = new TriggerService(mockKafkaClient);
    }

    /**
     * Ensure the Kafka client is called to emit events over a period of time.
     */
    @Test
    public void testProcess() throws Exception {
        TriggerEventsRequest testEvent = TriggerEventsRequest.builder()
                .periodToSendSeconds(2)
                .delayMilliseconds(10)
                .build();
        service.process(testEvent);
        verify(mockKafkaClient, atLeast(2)).sendMessage(anyString());
    }
}

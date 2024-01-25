package demo.integration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import demo.MonitoringDemoConfiguration;
import demo.domain.Item;
import demo.repository.ItemRepository;
import demo.rest.api.ItemCountResponse;
import demo.rest.api.TriggerEventsRequest;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { MonitoringDemoConfiguration.class } )
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@EmbeddedKafka(controlledShutdown = true, topics = { "demo-topic" })
public class EndToEndIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll();

        // Wait until the partitions are assigned.
        registry.getListenerContainers().stream().forEach(container ->
                ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic()));
    }

    /**
     * Trigger the application to emit a number of events, which the application then consumes and persists corresponding
     * records to the database for each.
     */
    @Test
    public void testFlow() {
        Long expectedItemCount = 4L;
        TriggerEventsRequest request = TriggerEventsRequest.builder()
                .periodToSendSeconds(1)
                .delayMilliseconds(275)
                .build();
        ResponseEntity<String> triggerEventsResponse = restTemplate.postForEntity("/v1/trigger", request, String.class);
        assertThat(triggerEventsResponse.getStatusCode(), equalTo(HttpStatus.ACCEPTED));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).pollDelay(2200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    ResponseEntity<ItemCountResponse> response = restTemplate.getForEntity("/v1/items/count", ItemCountResponse.class);
                    log.info("Expected {} items, found {} items.", expectedItemCount, response.getBody().getCount());
                    return response.getStatusCode().equals(HttpStatus.OK) && response.getBody().getCount()==expectedItemCount;
                });
    }
}

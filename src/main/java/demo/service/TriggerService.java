package demo.service;

import java.util.concurrent.TimeUnit;

import demo.event.DemoEvent;
import demo.lib.KafkaClient;
import demo.mapper.JsonMapper;
import demo.rest.api.TriggerEventsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TriggerService {

    @Autowired
    private final KafkaClient kafkaClient;

    /**
     * Processing happens asynchronously so the caller can return.
     *
     * Sends events for a set period of time.
     */
    @Async
    public void process(TriggerEventsRequest request) throws Exception {
        int counter = 0;
        log.info("Sending events for {} seconds with a delay between each send of {} milliseconds", request.getPeriodToSendSeconds(), request.getDelayMilliseconds());
        long start = System.currentTimeMillis();
        long end = start + (request.getPeriodToSendSeconds() * 1000);
        while (System.currentTimeMillis() < end) {
            sendEvent();
            counter++;
            if (counter % 100 == 0) {
                log.info("Total events sent to topic: {}", counter);
            }
            TimeUnit.MILLISECONDS.sleep(request.getDelayMilliseconds());
        }
        log.info("Total events sent to topic: {}", counter);
    }

    /**
     * Send an event.
     */
    private void sendEvent() {
        String payload = RandomStringUtils.randomAlphanumeric(100);

        DemoEvent demoEvent = DemoEvent.builder()
                .data(payload)
                .build();

        kafkaClient.sendMessage(JsonMapper.writeToJson(demoEvent));
    }
}

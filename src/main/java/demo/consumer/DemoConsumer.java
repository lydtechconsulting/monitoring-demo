package demo.consumer;

import java.util.concurrent.atomic.AtomicLong;

import demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DemoConsumer {

    final AtomicLong counter = new AtomicLong();

    @Autowired
    private final ItemService itemService;

    /**
     * Consumes events from any topic prefixed with "demo-".
     */
    @KafkaListener(topicPattern = "demo-topic", groupId = "demo-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload String payload) {
        counter.getAndIncrement();
        itemService.createItem(counter.longValue(), payload);
        if(counter.get() % 100 == 0){
            log.info("Total events received (each 100): {}", counter.get());
        }
    }
}

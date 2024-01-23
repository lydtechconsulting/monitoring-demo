package demo.consumer;

import demo.event.DemoEvent;
import demo.mapper.JsonMapper;
import demo.service.ItemService;
import demo.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DemoConsumerTest {

    private DemoConsumer consumer;
    private ItemService serviceMock;

    @BeforeEach
    public void setUp() {
        serviceMock = mock(ItemService.class);
        consumer = new DemoConsumer(serviceMock);
    }
    @Test
    public void testListen() {
        DemoEvent testEvent = TestData.buildDemoEvent(randomUUID().toString());
        String payload = JsonMapper.writeToJson(testEvent);

        consumer.listen(payload);

        assertThat(consumer.counter.get(), equalTo(1L));
        verify(serviceMock, times(1)).createItem(1L, payload);
    }
}

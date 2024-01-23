package demo.util;

import demo.event.DemoEvent;
import demo.rest.api.TriggerEventsRequest;

public class TestData {

    public static String INBOUND_DATA = "event data";

    public static DemoEvent buildDemoEvent(String id) {
        return DemoEvent.builder()
                .data(INBOUND_DATA)
                .build();
    }

    public static TriggerEventsRequest buildTriggerEventsRequest() {
        return TriggerEventsRequest.builder()
                .periodToSendSeconds(10)
                .delayMilliseconds(2)
                .build();
    }
}

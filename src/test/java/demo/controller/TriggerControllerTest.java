package demo.controller;

import demo.rest.api.TriggerEventsRequest;
import demo.service.TriggerService;
import demo.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TriggerControllerTest {

    private TriggerService serviceMock;
    private TriggerController controller;

    @BeforeEach
    public void setUp() {
        serviceMock = mock(TriggerService.class);
        controller = new TriggerController(serviceMock);
    }

    /**
     * Ensure that the REST request is successfully passed on to the service.
     */
    @Test
    public void testTrigger_Success() throws Exception {
        TriggerEventsRequest request = TestData.buildTriggerEventsRequest();
        ResponseEntity response = controller.trigger(request);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        verify(serviceMock, times(1)).process(request);
    }

    /**
     * If an exception is thrown, an internal server error is returned.
     */
    @Test
    public void testTrigger_ServiceThrowsException() throws Exception {
        TriggerEventsRequest request = TestData.buildTriggerEventsRequest();
        doThrow(new RuntimeException("Service failure")).when(serviceMock).process(request);
        ResponseEntity response = controller.trigger(request);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(serviceMock, times(1)).process(request);
    }
}

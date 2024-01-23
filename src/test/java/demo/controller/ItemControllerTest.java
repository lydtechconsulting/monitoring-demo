package demo.controller;

import java.util.Optional;

import demo.rest.api.ItemCountResponse;
import demo.rest.api.TriggerEventsRequest;
import demo.service.ItemService;
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
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemService serviceMock;
    private ItemController controller;

    @BeforeEach
    public void setUp() {
        serviceMock = mock(ItemService.class);
        controller = new ItemController(serviceMock);
    }

    @Test
    public void testGetCount() throws Exception {
        when(serviceMock.getItemCount()).thenReturn(10L);
        ResponseEntity<ItemCountResponse> response = controller.getItemCount();
        assertThat(response.getBody().getCount(), equalTo(10L));
        verify(serviceMock, times(1)).getItemCount();
    }
}

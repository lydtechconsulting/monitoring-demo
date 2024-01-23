package demo.service;

import demo.domain.Item;
import demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemServiceTest {

    private ItemRepository itemRepositoryMock;
    private ItemService service;

    @BeforeEach
    public void setUp() {
        itemRepositoryMock = mock(ItemRepository.class);
        service = new ItemService(itemRepositoryMock);
    }

    /**
     * Ensure the item is persisted.
     */
    @Test
    public void testProcess() {
        service.createItem(1L, "test-data");
        verify(itemRepositoryMock, times(1)).save(any(Item.class));
    }
}

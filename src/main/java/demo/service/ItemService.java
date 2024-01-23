package demo.service;

import demo.domain.Item;
import demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Transactional
    public void createItem(Long sequenceNumber, String data) {
        Item item = Item.builder()
                .sequenceNumber(sequenceNumber)
                .data(data)
                .build();
        itemRepository.save(item);
    }

    public Long getItemCount() {
        Long count = itemRepository.count();
        return count;
    }
}

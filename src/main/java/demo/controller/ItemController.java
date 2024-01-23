package demo.controller;

import demo.rest.api.ItemCountResponse;
import demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/items")
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @GetMapping("/count")
    public ResponseEntity<ItemCountResponse> getItemCount() {
        Long count = itemService.getItemCount();
        log.info("Total item count: " + count);
        return ResponseEntity.ok(ItemCountResponse.builder().count(count).build());
    }
}

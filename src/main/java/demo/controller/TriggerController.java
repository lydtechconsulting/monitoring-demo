package demo.controller;

import demo.rest.api.TriggerEventsRequest;
import demo.service.TriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/trigger")
public class TriggerController {

    @Autowired
    private final TriggerService service;

    /**
     * Trigger sending the requested number of events to the outbound topic.
     *
     * There is a delay between each event being sent, as configured on the request.
     */
    @PostMapping()
    public ResponseEntity<Void> trigger(@RequestBody TriggerEventsRequest request) {
        if(request.getPeriodToSendSeconds() == null || request.getPeriodToSendSeconds()<1) {
            log.error("Invalid period to send");
            return ResponseEntity.badRequest().build();
        }
        if(request.getDelayMilliseconds() == null || request.getDelayMilliseconds()<0) {
            log.error("Invalid delay milliseconds");
            return ResponseEntity.badRequest().build();
        }

        try {
            service.process(request);
            return ResponseEntity.accepted().build();
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

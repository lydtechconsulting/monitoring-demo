package demo.rest.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerEventsRequest {

    /**
     * The period in seconds over which to send events.
     */
    private Integer periodToSendSeconds;

    /**
     * The delay in milliseconds between each send.
     */
    private Integer delayMilliseconds;
}

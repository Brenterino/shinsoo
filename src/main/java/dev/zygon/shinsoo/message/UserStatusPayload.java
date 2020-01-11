package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusPayload {

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserStatus status = null;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> error = Collections.emptyList();
}

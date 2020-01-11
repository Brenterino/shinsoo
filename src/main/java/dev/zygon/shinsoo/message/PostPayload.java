package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPayload {

    @JsonIgnore
    public static final int UNKNOWN_POST = 0;

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private Post post = null;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> error = Collections.emptyList();
}

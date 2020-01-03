package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> response = Collections.emptyList();

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> error = Collections.emptyList();
}

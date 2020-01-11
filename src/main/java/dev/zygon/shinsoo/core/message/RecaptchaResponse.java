package dev.zygon.shinsoo.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecaptchaResponse {

    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTimestamp;

    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes;
}

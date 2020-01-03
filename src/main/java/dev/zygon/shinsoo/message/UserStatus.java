package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

    @Builder.Default
    @JsonProperty("logged_in")
    private boolean loggedIn = false;

    @Builder.Default
    private String username = "";

    @Builder.Default
    @JsonProperty("mapleid")
    private String mapleId = "";

    @Builder.Default
    @JsonProperty("gm_level")
    private int gmLevel = 0;
}

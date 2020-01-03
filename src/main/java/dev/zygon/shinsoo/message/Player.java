package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Builder.Default
    private String name = "";

    @Builder.Default
    private int level = 1;

    @Builder.Default
    private long exp = 0;

    @Builder.Default
    private long fame = 0;

    @Builder.Default
    @JsonProperty("job_id")
    private long job = 0;

    @Builder.Default
    @JsonProperty("guild_name")
    private String guild = null;
}

package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerList {

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("alert")
    private String bannerMessage = null;

    @Builder.Default
    @JsonProperty("online_count")
    private int online = 0;

    @Builder.Default
    @JsonProperty("server_status")
    private List<Server> statuses = Collections.emptyList();
}

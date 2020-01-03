package dev.zygon.shinsoo.message;

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
public class ServerList {

    @Builder.Default
    @JsonProperty("online_count")
    private int online = 0;

    @Builder.Default
    @JsonProperty("server_status")
    private List<Server> statuses = Collections.emptyList();
}

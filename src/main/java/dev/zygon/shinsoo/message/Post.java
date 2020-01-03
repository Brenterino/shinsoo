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
public class Post {

    @Builder.Default
    private long id = 0;

    @Builder.Default
    private String type = "";

    @Builder.Default
    private long views = 0;

    @Builder.Default
    private String title = "";

    @Builder.Default
    private String author = "";

    @Builder.Default
    @JsonProperty("created_at")
    private String createdTime = "";

    @Builder.Default
    @JsonProperty("updated_at")
    private String updatedTime = "";

    @Builder.Default
    private String content = "";
}

package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostList extends Paginated {

    @Builder.Default
    @JsonProperty("data")
    private List<Post> posts = Collections.emptyList();
}

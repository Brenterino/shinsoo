package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( { "prev", "current", "next", "last" })
public class PaginatedError extends Paginated {

    @Builder.Default
    private List<String> error = Collections.emptyList();
}

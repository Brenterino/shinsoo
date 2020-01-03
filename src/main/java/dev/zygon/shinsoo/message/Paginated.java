package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paginated {

    @JsonIgnore
    public static final int DEFAULT_PAGE = 1;

    @JsonIgnore
    public static final int DEFAULT_PAGE_SIZE = 5;

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    private long prev = 0;

    @Builder.Default
    private long current = 0;

    @Builder.Default
    private long next = 0;

    @Builder.Default
    private long last = 0;
}

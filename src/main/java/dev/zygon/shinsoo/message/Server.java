package dev.zygon.shinsoo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @Builder.Default
    private String name = "";

    @Builder.Default
    private boolean status = false;
}

package dev.zygon.shinsoo.message;

import lombok.*;

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

package dev.zygon.shinsoo.core.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private String username;
    private String mapleId;
    private String email;
    private String password;
    private int gmLevel;
}

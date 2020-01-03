package dev.zygon.shinsoo.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

package dev.zygon.shinsoo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinCredentials {

    @FormParam("name")
    @PartType(MediaType.TEXT_PLAIN)
    private String name;

    @FormParam("email")
    @PartType(MediaType.TEXT_PLAIN)
    private String email;

    @FormParam("mapleid")
    @PartType(MediaType.TEXT_PLAIN)
    private String mapleId;

    @FormParam("password")
    @PartType(MediaType.TEXT_PLAIN)
    private String password;

    @FormParam("password_confirmation")
    @PartType(MediaType.TEXT_PLAIN)
    private String confirmation;

    @FormParam("recaptcha")
    @PartType(MediaType.TEXT_PLAIN)
    private String token;
}

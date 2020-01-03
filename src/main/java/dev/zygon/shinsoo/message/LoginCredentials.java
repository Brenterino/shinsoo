package dev.zygon.shinsoo.message;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {

    @FormParam("email")
    @PartType(MediaType.TEXT_PLAIN)
    private String email;

    @FormParam("password")
    @PartType(MediaType.TEXT_PLAIN)
    private String password;
}

package dev.zygon.shinsoo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

/**
 * Message which is sent by the front end when the user submits
 * the reset password form.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetCredentials {

    @FormParam("mapleid")
    @PartType(MediaType.TEXT_PLAIN)
    private String mapleId;

    @FormParam("email")
    @PartType(MediaType.TEXT_PLAIN)
    private String email;

    @FormParam("password")
    @PartType(MediaType.TEXT_PLAIN)
    private String password;

    @FormParam("password_confirmation")
    @PartType(MediaType.TEXT_PLAIN)
    private String confirmation;
}

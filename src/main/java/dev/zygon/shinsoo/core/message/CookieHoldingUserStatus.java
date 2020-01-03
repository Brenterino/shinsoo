package dev.zygon.shinsoo.core.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.zygon.shinsoo.message.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.core.NewCookie;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CookieHoldingUserStatus extends UserStatus {

    @JsonIgnore
    private NewCookie cookie;
}

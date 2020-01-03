package dev.zygon.shinsoo.security;

import dev.zygon.shinsoo.message.UserStatus;

public interface SecuredSession {

    UserStatus status();

    UserStatus begin(UserStatus status);

    UserStatus end();
}

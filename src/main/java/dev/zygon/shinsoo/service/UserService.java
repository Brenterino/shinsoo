package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.*;

public interface UserService {

    UserStatus session();

    UserStatusPayload login(LoginCredentials credentials);

    UserStatusPayload logout();
}

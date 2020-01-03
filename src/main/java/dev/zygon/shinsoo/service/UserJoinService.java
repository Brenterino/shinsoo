package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.message.JoinResponse;

public interface UserJoinService {

    JoinResponse join(JoinCredentials credentials);
}

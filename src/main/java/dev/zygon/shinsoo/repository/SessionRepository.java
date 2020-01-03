package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.message.UserStatus;

public interface SessionRepository {

    boolean sessionActive(String nonce) throws Exception;

    UserStatus session(String nonce) throws Exception;

    boolean beginSession(String nonce, UserStatus status) throws Exception;

    boolean endSession(String nonce) throws Exception;
}

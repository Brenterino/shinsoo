package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.core.dto.UserDetails;

public interface UserRepository {

    boolean userExists(String username) throws Exception;

    boolean idExists(String id) throws Exception;

    boolean emailExists(String email) throws Exception;

    boolean create(UserDetails details) throws Exception;

    UserDetails detailsForEmail(String email) throws Exception;
}

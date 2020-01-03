package dev.zygon.shinsoo.security;

public interface TokenValidator {

    boolean valid(String token);
}

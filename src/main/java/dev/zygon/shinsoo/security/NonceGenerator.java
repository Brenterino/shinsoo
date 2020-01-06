package dev.zygon.shinsoo.security;

public interface NonceGenerator {

    String nonce(String metadata);
}

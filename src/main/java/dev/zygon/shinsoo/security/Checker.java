package dev.zygon.shinsoo.security;

public interface Checker {

    boolean check(String provided, String saved);
}

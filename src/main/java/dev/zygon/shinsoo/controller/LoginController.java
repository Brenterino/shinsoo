package dev.zygon.shinsoo.controller;

import dev.zygon.shinsoo.message.LoginCredentials;

import javax.ws.rs.core.Response;

public interface LoginController {

    Response login(LoginCredentials credentials);
}

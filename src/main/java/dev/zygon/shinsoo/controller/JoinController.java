package dev.zygon.shinsoo.controller;

import dev.zygon.shinsoo.message.JoinCredentials;

import javax.ws.rs.core.Response;

public interface JoinController {

    Response join(JoinCredentials credentials);
}

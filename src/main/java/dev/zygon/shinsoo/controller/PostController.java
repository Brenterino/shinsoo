package dev.zygon.shinsoo.controller;

import javax.ws.rs.core.Response;

public interface PostController {

    Response post();

    Response post(long id);
}

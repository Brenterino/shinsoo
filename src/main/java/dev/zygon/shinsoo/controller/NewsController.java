package dev.zygon.shinsoo.controller;

import javax.ws.rs.core.Response;

public interface NewsController {

    Response news();

    Response news(long page);
}

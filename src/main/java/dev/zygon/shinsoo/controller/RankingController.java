package dev.zygon.shinsoo.controller;

import javax.ws.rs.core.Response;

public interface RankingController {

    Response rankings();

    Response rankings(long page);
}

package dev.zygon.shinsoo.controller;

import javax.ws.rs.core.Response;

public interface RankingController {

    Response rankings();

    Response rankings(long page);

    Response overallRankings();

    Response overallRankings(long page);

    Response jobRankings();

    Response jobRankings(String job);

    Response jobRankings(String job, long page);

    Response fameRankings();

    Response fameRankings(long page);

    Response searchRankings();

    Response searchRankings(String query);
}

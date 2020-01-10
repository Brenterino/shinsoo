package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.PlayerListPayload;

public interface RankingService {

    Paginated rankings(long page);

    Paginated jobRankings(String job, long page);

    Paginated fameRankings(long page);

    PlayerListPayload searchRankings(String query);
}

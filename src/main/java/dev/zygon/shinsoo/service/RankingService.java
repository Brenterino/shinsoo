package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.Paginated;

public interface RankingService {

    Paginated rankings(long page);
}

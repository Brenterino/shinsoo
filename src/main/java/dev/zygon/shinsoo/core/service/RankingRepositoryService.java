package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.PaginatedError;
import dev.zygon.shinsoo.message.Player;
import dev.zygon.shinsoo.message.Ranking;
import dev.zygon.shinsoo.service.RankingService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;

@ApplicationScoped
public class RankingRepositoryService implements RankingService {

    private Player player;
    private Ranking ranking;

    public RankingRepositoryService() {
        player = Player.builder()
                .name("SwagAngel69")
                .fame(-69)
                .guild("Solaxia")
                .build();
        ranking = Ranking.builder()
                .success(true)
                .prev(1)
                .current(1)
                .next(1)
                .last(1)
                .players(Collections.singletonList(player))
                .build();
    }

    @Override
    public Paginated rankings(long page) {
        if (page == 0)
            return PaginatedError.builder()
                    .success(false)
                    .error(Collections.singletonList("No results"))
                    .build();
        return ranking;
    }
}

package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.*;
import dev.zygon.shinsoo.repository.PlayerRepository;
import dev.zygon.shinsoo.service.RankingService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.singletonList;

@Slf4j
@ApplicationScoped
public class RankingRepositoryService implements RankingService {

    @Inject
    PlayerRepository repository;

    @Override
    public Paginated rankings(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page < 0 || page > totalPages)
                return createFailedPagination("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.players(offset, Paginated.DEFAULT_PAGE_SIZE);
                return createPaginatedRankings(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated rankings from repository.", ex);
            return createFailedPagination("Something went wrong when loading from the database!");
        }
    }

    private Paginated createPaginatedRankings(List<Player> players, long page, long totalPages) {
        return Ranking.builder()
                .success(true)
                .prev(Math.max(page - 1, 1))
                .current(page)
                .next(Math.min(page + 1, totalPages))
                .last(totalPages)
                .players(players)
                .build();
    }

    private Paginated createFailedPagination(String message) {
        return Paginated.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }
}

package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.*;
import dev.zygon.shinsoo.repository.PlayerRepository;
import dev.zygon.shinsoo.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.singletonList;

@Slf4j
@ApplicationScoped
public class RankingRepositoryService implements RankingService {

    @ConfigProperty(name = "shinsoo.query.size.min", defaultValue = "4")
    long minimumQuerySize;

    @ConfigProperty(name = "shinsoo.query.size.max", defaultValue = "12")
    long maximumQuerySize;

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

    @Override
    public Paginated jobRankings(String job, long page) {
        JobRange range = JobRange.fromName(job);
        if (range == JobRange.UNKNOWN)
            return createFailedPagination("Select a job.");

        try {
            long totalPages = (long) Math.ceil(repository.countJob(range) / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page < 0 || page > totalPages)
                return createFailedPagination("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.playersByJob(range, offset, Paginated.DEFAULT_PAGE_SIZE);
                return createPaginatedRankings(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated job rankings from repository.", ex);
            return createFailedPagination("Something went wrong when loading from the database!");
        }
    }

    @Override
    public Paginated fameRankings(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page < 0 || page > totalPages)
                return createFailedPagination("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.playersByFame(offset, Paginated.DEFAULT_PAGE_SIZE);
                return createPaginatedRankings(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated job rankings from repository.", ex);
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

    @Override
    public PlayerListPayload searchRankings(String query) {
        if (query.length() < minimumQuerySize || query.length() > maximumQuerySize)
            return createFailedPayload(String.format("Character name must be between %d and %d.",
                    minimumQuerySize,
                    maximumQuerySize));

        try {
            List<Player> players = repository.searchByQuery(query);
            if (players.isEmpty())
                return createFailedPayload("No characters match " + query + ".");
            return createPayloadResults(players);
        } catch (Exception ex) {
            log.error("Unable to search for player(s) from repository.", ex);
            return createFailedPayload("Something went wrong when loading from the database!");
        }
    }

    private PlayerListPayload createPayloadResults(List<Player> players) {
        return PlayerListPayload.builder()
                .success(true)
                .players(players)
                .build();
    }

    private PlayerListPayload createFailedPayload(String message) {
        return PlayerListPayload.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }
}

/*
    Shinsoo: Java-Quarkus Back End for Aria
    Copyright (C) 2020  Brenterino <brent@zygon.dev>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.*;
import dev.zygon.shinsoo.repository.PlayerRepository;
import dev.zygon.shinsoo.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static dev.zygon.shinsoo.message.Paginated.failure;
import static dev.zygon.shinsoo.message.Paginated.success;
import static java.util.Collections.singletonList;

/**
 * Implementation for {@link RankingService} which utilizes
 * {@link PlayerRepository} to retrieve players from a data
 * repository.  Generates response messages based on the
 * data retrieved from the repository.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
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
    public Paginated<?> rankings(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page <= 0 || page > totalPages)
                return failure("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.players(offset, Paginated.DEFAULT_PAGE_SIZE);
                return success(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated rankings from repository.", ex);
            return failure("Unable to load rankings. Please try again later.");
        }
    }

    @Override
    public Paginated<?> jobRankings(String job, long page) {
        JobRange range = JobRange.fromName(job);
        if (range == JobRange.UNKNOWN)
            return failure("Select a job.");

        try {
            long totalPages = (long) Math.ceil(repository.countJob(range) / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page <= 0 || page > totalPages)
                return failure("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.playersByJob(range, offset, Paginated.DEFAULT_PAGE_SIZE);
                return success(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated job rankings from repository.", ex);
            return failure("Unable to load rankings. Please try again later.");
        }
    }

    @Override
    public Paginated<?> fameRankings(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page <= 0 || page > totalPages)
                return failure("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Player> players = repository.playersByFame(offset, Paginated.DEFAULT_PAGE_SIZE);
                return success(players, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated job rankings from repository.", ex);
            return failure("Unable to load rankings. Please try again later.");
        }
    }

    @Override
    public PlayerList searchRankings(String query) {
        if (query.length() < minimumQuerySize || query.length() > maximumQuerySize)
            return createFailedPayload(String.format("Character name length must be between %d and %d.",
                    minimumQuerySize,
                    maximumQuerySize));

        try {
            List<Player> players = repository.searchByQuery(query);
            if (players.isEmpty())
                return createFailedPayload("No characters match " + query + ".");
            else
                return createPayloadResults(players);
        } catch (Exception ex) {
            log.error("Unable to search for player(s) from repository.", ex);
            return createFailedPayload("Unable to find players. Please try again later.");
        }
    }

    private PlayerList createFailedPayload(String message) {
        return PlayerList.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private PlayerList createPayloadResults(List<Player> players) {
        return PlayerList.builder()
                .success(true)
                .players(players)
                .build();
    }
}

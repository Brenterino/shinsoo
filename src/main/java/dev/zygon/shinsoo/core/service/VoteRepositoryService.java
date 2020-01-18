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

import dev.zygon.shinsoo.core.validation.VotePingbackValidator;
import dev.zygon.shinsoo.message.VotePingback;
import dev.zygon.shinsoo.repository.VoteRepository;
import dev.zygon.shinsoo.service.VoteService;
import dev.zygon.shinsoo.validation.Failures;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Implementation for {@link VoteService} which uses
 * {@link VoteRepository} in order to determine if
 * the provided request from the pingback is valid.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@ApplicationScoped
public class VoteRepositoryService implements VoteService {

    private static final long HOURS_TO_MILLISECONDS = 3600000;

    @ConfigProperty(name = "shinsoo.vote.pingback.authorized.sources")
    Optional<String> authorizedSources;

    @ConfigProperty(name = "shinsoo.vote.delay.between.votes.hours", defaultValue = "24")
    Long delayBetweenVotesHours;

    @Inject
    VoteRepository repository;

    @Override
    public boolean process(VotePingback pingback) {
        VotePingbackValidator validator = new VotePingbackValidator(pingback,
                authorizedSources.orElse(""));
        if (validator.validate() == Failures.NONE)
            return processAuthorizedVote(pingback);
        else
            return false;
    }

    private boolean processAuthorizedVote(VotePingback pingback) {
        try {
            if (repository.canVote(pingback.getMapleId(), delayHoursToMilliseconds())) {
                return repository.addVote(pingback.getMapleId());
            }
        } catch (Exception ex) {
            log.error("Unable to process vote with repository.", ex);
        }
        return false;
    }

    private long delayHoursToMilliseconds() {
        return delayBetweenVotesHours * HOURS_TO_MILLISECONDS;
    }
}

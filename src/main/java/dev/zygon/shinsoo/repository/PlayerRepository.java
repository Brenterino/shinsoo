package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Player;

import java.util.List;

public interface PlayerRepository {

    long count() throws Exception;

    List<Player> players(long offset, long limit) throws Exception;

    List<Player> playersByFame(long offset, long limit) throws Exception;

    long countJob(JobRange range) throws Exception;

    List<Player> playersByJob(JobRange range, long offset, long limit) throws Exception;

    List<Player> searchByQuery(String query) throws Exception;
}

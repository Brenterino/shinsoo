package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.message.Player;

import java.util.List;

public interface PlayerRepository {

    long count() throws Exception;

    List<Player> players(long offset, long limit) throws Exception;
}

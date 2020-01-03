package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.message.Post;

import java.util.List;

public interface PostRepository {

    List<Post> posts(long offset, long limit) throws Exception;
}

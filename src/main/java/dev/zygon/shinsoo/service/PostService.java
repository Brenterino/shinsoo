package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.PostPayload;

public interface PostService {

    PostPayload post(long id);

    Paginated posts(long page);
}

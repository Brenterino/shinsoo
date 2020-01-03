package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.Paginated;

public interface NewsService {

    Paginated news(long page);
}

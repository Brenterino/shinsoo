package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.repository.PostRepository;
import dev.zygon.shinsoo.service.NewsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NewsRepositoryService implements NewsService {

    @Inject
    PostRepository repository;

    @Override
    public Paginated news(long page) {
        // TODO create paginated response
        return null;
    }
}

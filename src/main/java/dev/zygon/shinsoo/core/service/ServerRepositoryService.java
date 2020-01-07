package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.Server;
import dev.zygon.shinsoo.message.ServerList;
import dev.zygon.shinsoo.service.ServerService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Optional;

@ApplicationScoped
public class ServerRepositoryService implements ServerService {

    @ConfigProperty(name = "shinsoo.banner.message")
    Optional<String> message;

    @Override
    public ServerList servers() {
        ServerList status = new ServerList();
        status.setBannerMessage(message.orElse(null));
        status.setStatuses(new ArrayList<>(4));
        status.getStatuses().add(new Server("Login", true));
        status.getStatuses().add(new Server("Channel 1", true));
        status.getStatuses().add(new Server("Channel 2", false));
        status.getStatuses().add(new Server("Yeet", false));
        return status;
    }
}

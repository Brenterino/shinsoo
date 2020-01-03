package dev.zygon.shinsoo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/app")
public class App {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String app() {
        return "Written with Quarkus 1.1.0 - By: Zygon";
    }
}

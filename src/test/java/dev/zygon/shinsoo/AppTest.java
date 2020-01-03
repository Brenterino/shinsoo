package dev.zygon.shinsoo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AppTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/app")
          .then()
             .statusCode(200)
             .body(is("Written with Quarkus 1.1.0 - By: Zygon"));
    }

}
package com.reba.personcrudapi.gatling.performance.test.com.reba.personcrudapi;

import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class PersonSimulation extends Simulation {
    {
        var test = exec(http("test").get("/people"));
        var httpProtocol = http.baseUrl("http://localhost:8080/api")
                .header("Content-Type", "application/json")
                .header("Accept", "*/*");

        var scenarioBuilder1 = scenario("Test performance 1").exec(test);
        var scenarioBuilder2 = scenario("Test performance 2").exec(test);

        setUp(
                scenarioBuilder1.injectOpen(constantUsersPerSec(1).during(1)),
                scenarioBuilder2.injectOpen(constantUsersPerSec(500).during(1))
        ).protocols(httpProtocol);
    }
}

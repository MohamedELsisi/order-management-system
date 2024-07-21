package com.qeema.engineering.it;


import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT {

    @LocalServerPort
    int port;
    String requestValidBody;
    String overQuantityRequestBody;

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.port = port;
        RestAssured.basePath = "/orderManagement/v1";
        requestValidBody = new String(Files.readAllBytes(Paths.get("src/test/resources/it/validOrder.json")));
        overQuantityRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/it/nonValidPrice.json")));
    }

    @Test
    void whenGetAllOrders_expect200() {
        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void whenPostValidOrder_expect201() {

        given()
                .contentType("application/json")
                .when()
                .body(requestValidBody)
                .post("/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void whenPostInvalidPrice_expect400() {
        given()
                .contentType("application/json")
                .when()
                .body(overQuantityRequestBody)
                .post("/orders")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}

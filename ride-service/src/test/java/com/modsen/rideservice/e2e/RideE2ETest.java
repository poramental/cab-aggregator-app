package com.modsen.rideservice.e2e;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.modsen.rideservice.RideServiceApplication;
import com.modsen.rideservice.config.ContainerConfigJunit5;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.exception.AppError;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.modsen.rideservice.util.IntegrationTestUtil.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@ExtendWith(WireMockExtension.class)
@Sql(
        scripts = {
                "classpath:sql/ride/delete-data.sql",
                "classpath:sql/ride/insert-data.sql"
        }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RideServiceApplication.class)
public class RideE2ETest {

    private WireMockServer driverServer;

    @LocalServerPort
    private int port;

    private final DriverResponse driverResponse = getDefaultDriverResponse();

    private Response response;

    static {
        ContainerConfigJunit5.run();
    }

    public void setup() {
        driverServer = new WireMockServer(9002);
        driverServer.start();

        driverServer
                .stubFor(get(urlPathEqualTo(DRIVER_PATH + "/1"))
                        .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                                .withHeader("content-type", "application/json")
                                .withBody(fromObjectToString(driverResponse))));
        driverServer
                .stubFor(get(urlPathEqualTo(DRIVER_PATH + "/2"))
                        .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                                .withHeader("content-type", "application/json")
                                .withBody(fromObjectToString(driverResponse.setId(2L).setIsInRide(true)))));

        driverServer
                .stubFor(get(urlPathEqualTo(DRIVER_PATH + "/3"))
                        .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                                .withHeader("content-type", "application/json")
                                .withBody(fromObjectToString(driverResponse.setId(3L).setIsInRide(false)))));
        driverServer
                .stubFor(post(urlPathMatching("/api/v1/drivers/[1-9]/is-in-ride"))
                        .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

    }

    @When("the driver with id {int} attempts to accept the ride")
    public void driverAttemptsToAcceptRide1(int driverId) {
        setup();
        response = given()
                .port(port)
                .queryParam("driverId", driverId)
                .and()
                .queryParam("rideId", NOT_ACCEPTED_RIDE_ID_WAITING_FOR_DRIVER_ID_1L.toString())
                .when()
                .patch(RIDE_ACCEPT_PATH);
    }

    @When("the driver with id {int} attempts to accept a ride that is waiting for another driver")
    public void driverAttemptsToAcceptRide2(int driverId) {
        setup();
        response = given()
                .port(port)
                .queryParam("driverId", driverId)
                .and()
                .queryParam("rideId", NOT_ACCEPTED_RIDE_ID_WAITING_FOR_DRIVER_ID_2L)
                .when()
                .patch(RIDE_ACCEPT_PATH);
    }

    @When("the driver with id {int} attempts to accept the ride that already accepted by another driver")
    public void driverAttemptsToAcceptRide3(int driverId) {
        setup();
        response = given()
                .port(port)
                .queryParam("driverId", driverId)
                .and()
                .queryParam("rideId", RIDE_ID_WAITING_FOR_DRIVER_ID_3L_AND_HAVE_ANOTHER_DRIVER)
                .when()
                .patch(RIDE_ACCEPT_PATH);
    }

    @Then("the response should have status code {int}")
    public void responseShouldHaveStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the driver id in the response should be {int}")
    public void driverIdInTheResponseShouldBe(int expectedDriverId) {
        RideResponse rideResponse = response.as(RideResponse.class);
        assertEquals(expectedDriverId, (long) rideResponse.getDriverId());
    }

    @Then("the error message in the response should be {string}")
    public void errorMessageInTheResponseShouldBe(String expectedErrorMessage) {
        AppError appError = response.as(AppError.class);
        assertEquals(expectedErrorMessage, appError.getMessage());
    }


    public static <T> String fromObjectToString(T object) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

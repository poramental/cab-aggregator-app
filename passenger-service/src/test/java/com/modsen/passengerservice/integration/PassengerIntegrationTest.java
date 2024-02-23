package com.modsen.passengerservice.integration;


import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.AppError;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.util.ExceptionMessages;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;

import static com.modsen.passengerservice.util.IntegrationTestUtil.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(
        scripts = {
                "classpath:sql/passenger/delete-data.sql",
                "classpath:sql/passenger/insert-data.sql"
        }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class PassengerIntegrationTest extends ContainerConfiguration {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    @LocalServerPort
    private int port;

    @Test
    void findById_shouldReturnPassengerNotFound_whenPassengerNotExist() {
        AppError expected = AppError.builder()
                .message(String.format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION, ID_NOT_FOUND))
                .build();

        var actual = given()
                .port(port)
                .pathParam(PATH_PARAM_ID, ID_NOT_FOUND)
                .when()
                .get(PATH_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(AppError.class);

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void findAll_shodReturnPassengersListResponse() {
        List<Passenger> allPassengers = passengerRepository.findAll();
        List<PassengerResponse> passengerResponseList = allPassengers.stream()
                .map(passengerMapper::entityToResponse)
                .toList();

        ListPassengerResponse expected = new ListPassengerResponse(passengerResponseList);

        var actual = given()
                .port(port)
                .when()
                .get(PATH_DEFAULT)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ListPassengerResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createPassenger_shouldReturnPassengerResponse_whenPassengerValid() {
        PassengerResponse expected = getCreatePassengerResponse();
        PassengerRequest passengerRequest = getCreatePassengerRequest();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .post(PATH_DEFAULT)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(PassengerResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnDriverResponse_whenPassengerExist() {
        PassengerResponse expected = getExpectedUpdatePassengerResponse();
        PassengerRequest updateRequest = getUpdatePassengerRequest();

        var actual = given()
                .port(port)
                .body(updateRequest)
                .pathParam(PATH_PARAM_ID, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .when()
                .put(PATH_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponse.class);

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void createPassenger_WhenPassengerEmailIsNotValid_ShouldReturnBadRequest() {
        PassengerRequest passengerRequest = getCreatePassengerRequest();
        passengerRequest.setEmail(NOT_VALID_EMAIL);

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when()
                .post(PATH_DEFAULT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(HashMap.class);

        assertThat(actual.get("email")).isEqualTo("email.invalid");
        assertThat(actual.containsKey("email")).isEqualTo(true);
    }


    @Test
    void deletePassenger_shouldReturnNoContent_whenPassengerExist() {

        given()
                .port(port)
                .pathParam(PATH_PARAM_ID, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}

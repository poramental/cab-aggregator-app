package com.modsen.driverservice.integration;

import com.modsen.driverservice.dto.DriverRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.dto.ListDriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.util.ExceptionMessage;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import com.modsen.driverservice.exception.AppError;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


import java.util.HashMap;
import java.util.List;

import static com.modsen.driverservice.util.IntegrationTestUtil.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Sql(
        scripts = {
                "classpath:sql/driver/delete-data.sql",
                "classpath:sql/driver/insert-data.sql"
        }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)


public class DriverIntegrationTest extends ContainerConfiguration {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverMapper driverMapper;

    @LocalServerPort
    private int port;

    @Test
    void findById_shouldReturnDriverNotFound_whenDriverNotExist() {
        AppError expected = AppError.builder()
                .message(String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, ID_NOT_FOUND))
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
    void findById_shouldReturnDriverNotFoundMessage_whenDriverNotExist() {
        AppError expected = AppError.builder()
                .message(String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, ID_NOT_FOUND))
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
    void findAllShodReturnDriverListResponse() {
        List<Driver> allDrivers = driverRepository.findAll();
        List<DriverResponse> driverResponseList = allDrivers.stream()
                .map(driverMapper::entityToResp)
                .toList();

        ListDriverResponse expected = new ListDriverResponse(driverResponseList);

        var actual = given()
                .port(port)
                .when()
                .get(FIND_ALL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ListDriverResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createDriverShouldReturnDriverResponseWhenDriverValid() {
        DriverResponse expected = getCreateDriverResponse();
        DriverRequest driverRequest = getCreateDriverRequest();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when()
                .post(PATH_DEFAULT)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    void createDriverWhenDriverEmailIsNotValidShouldReturnBadRequest() {
        DriverRequest driverRequest = getCreateDriverRequest();
        driverRequest.setEmail(NOT_VALID_EMAIL);

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(driverRequest)
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
    void tryCreateWhenDriverIsPresentShouldReturnStatusConflict() {
        DriverRequest driverRequest = getCreateDriverRequest();
        driverRequest.setEmail(DEFAULT_EMAIL);
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when()
                .post(PATH_DEFAULT)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract();
    }

    @Test
    void changeStatusForDriverWhenDriverExist() {


        var actual = given()
                .port(port)
                .pathParam(PATH_PARAM_ID, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .when()
                .post(CHANGE_STATUS_URI)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual.getIsInRide()).isEqualTo(false);

    }

    @Test
    void updateDriverWhenDriverExistShouldReturnDriverResponse() {
        DriverResponse expected = getExpectedUpdateDriverResponse();
        DriverRequest updateRequest = getUpdateDriverRequest();

        var actual = given()
                .port(port)
                .body(updateRequest)
                .pathParam(PATH_PARAM_ID, ID_FOR_UPDATE)
                .contentType(ContentType.JSON)
                .when()
                .put(PATH_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteDriverWhenDriverExist() {

        given()
                .port(port)
                .pathParam(PATH_PARAM_ID, ID_FOR_DELETE)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

}
package com.modsen.rideservice.integration;

import com.modsen.rideservice.config.ContainerConfiguration;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.modsen.rideservice.util.IntegrationTestUtil.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Sql(
        scripts = {
                "classpath:sql/ride/delete-data.sql",
                "classpath:sql/ride/insert-data.sql"
        }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class RideIntegrationTest extends ContainerConfiguration {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private RideMapper rideMapper;

    @LocalServerPort
    private int port;

    @Test
    void findAll_shouldReturnAllRides() {
        List<Ride> rides = rideRepository.findAll();

        List<RideResponse> expected = rides.stream()
                .map(rideMapper::entityToResponse)
                .toList();

        var actual = given()
                .port(port)
                .when()
                .get(GET_ALL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ListRideResponse.class);

        assertThat(actual.getRideResponseList().get(0).getId()).isEqualTo(expected.get(0).getId());
    }

    @Test
    void findRideById_shouldReturnRideResponse_whenRideExist() {
        Ride ride = rideRepository.findById(DEFAULT_RIDE_ID).get();
        RideResponse expected = rideMapper.entityToResponse(ride);

        var actual = given()
                .port(port)
                .pathParam(ID, DEFAULT_RIDE_ID)
                .when()
                .get(PATH_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponse.class);
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void getAllRidesByPassengerId_shouldReturnRideListResponse() {
        List<Ride> rideList = rideRepository.findAllByPassenger(DEFAULT_ID);
        List<RideResponse> rideResponses = rideList.stream()
                .map(rideMapper::entityToResponse)
                .toList();


        ListRideResponse expected = new ListRideResponse(rideResponses);


        var actual = given()
                .port(port)
                .queryParam("passengerId", DEFAULT_ID)
                .when()
                .get(PATH_GET_BY_PASSENGER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ListRideResponse.class);

        assertThat(actual.getRideResponseList().get(0).getPassenger())
                .isEqualTo(expected.getRideResponseList().get(0).getPassenger());
    }

    @Test
    void getAllRidesByDriverId_shouldReturnRideListResponse() {
        List<Ride> rideList = rideRepository.findAllByDriverId(DEFAULT_ID);
        List<RideResponse> rideResponses = rideList.stream()
                .map(rideMapper::entityToResponse)
                .toList();

        ListRideResponse expected = new ListRideResponse(rideResponses);

        var actual = given()
                .port(port)
                .queryParam("driverId", DEFAULT_ID)
                .when()
                .get(PATH_GET_BY_DRIVER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ListRideResponse.class);

        assertThat(actual.getRideResponseList().get(0).getDriverId())
                .isEqualTo(expected.getRideResponseList().get(0).getDriverId());
    }
}
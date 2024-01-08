package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.RideRespDto;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.RideAlreadyHaveDriverException;
import com.modsen.rideservice.exceptions.RideNotFoundException;
import com.modsen.rideservice.mappers.RideMapper;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository repository;

    private final RideMapper mapper;

    public ResponseEntity<List<RideRespDto>> getAll() {
        return new ResponseEntity<>(repository.findAll().stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<RideRespDto> getById(Long id) throws RideNotFoundException{
        return new ResponseEntity<>(
                mapper.entityToRespDto(
                        repository.findById(id).orElseThrow(() -> new RideNotFoundException(
                        String.format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,id)))),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<RideRespDto>> getAllPassengerRidesById(Long passengerId) {
        return new ResponseEntity<>(repository.findAllByPassengerId(passengerId)
                .stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<RideRespDto>> getAllDriverRidesById(Long driverId) {
        return new ResponseEntity<>(repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public HttpStatus acceptRide(Long rideId, Long driverId)
            throws RideNotFoundException,
            RideAlreadyHaveDriverException {
        Optional<Ride> ride_opt = repository.findById(rideId);
        if(ride_opt.isPresent()){
            if(Objects.nonNull(ride_opt.get().getDriverId()))
                throw new RideAlreadyHaveDriverException(String
                        .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION, rideId));
            repository.save(ride_opt.get().setDriverId(driverId));
            return HttpStatus.OK;
        }else
            throw new RideNotFoundException(String
                .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId));

    }
}

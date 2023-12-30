package com.modsen.passengerservice.services;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.entities.Passenger;
import com.modsen.passengerservice.mappers.PassengerMapper;
import com.modsen.passengerservice.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;


    public ResponseEntity<List<PassengerDto>> getAll(){
        return new ResponseEntity<>(passengerRepository.findAll().stream()
                .map(passenger -> passengerMapper.entityToDto(passenger))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public HttpStatus addPassenger(PassengerDto passengerDto){
        Optional<Passenger> passenger_opt_email = passengerRepository.findByEmail(passengerDto.getEmail());
        Optional<Passenger> passenger_opt_phone = passengerRepository.findByEmail(passengerDto.getPhone());
        Optional<Passenger> passenger_opt_username = passengerRepository.findByEmail(passengerDto.getUsername());
        if(passenger_opt_email.isPresent() ||
                passenger_opt_phone.isPresent() ||
                passenger_opt_username.isPresent()){
            //TODO make a exception for this block
            return HttpStatus.CONFLICT;
        }else {
            passengerRepository.save(passengerMapper.dtoToEntity(passengerDto));
            return HttpStatus.OK;
        }
    }
}

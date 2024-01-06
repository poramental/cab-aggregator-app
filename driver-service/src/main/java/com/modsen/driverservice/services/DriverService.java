package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.entities.Driver;
import com.modsen.driverservice.exceptions.DriverAlreadyExistException;
import com.modsen.driverservice.exceptions.DriverNotFoundException;
import com.modsen.driverservice.exceptions.RatingException;
import com.modsen.driverservice.exceptions.SortTypeException;
import com.modsen.driverservice.mappers.DriverMapper;
import com.modsen.driverservice.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverMapper driverMapper;

    private final DriverRepository driverRepository;

    public ResponseEntity<List<DriverDto>> getAll(){
        return new ResponseEntity<>(driverRepository.findAll().stream()
                .map(driverMapper::entityToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    public HttpStatus add(DriverDto driverDto) throws DriverAlreadyExistException {
        checkDriverParamsExist(driverDto.getEmail(),driverDto.getPhone());
        driverRepository.save(driverMapper.dtoToEntity(driverDto));
        return HttpStatus.OK;

    }

    public HttpStatus deleteByPhone(String phone) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByPhone(phone);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }else
            throw new DriverNotFoundException(String
                    .format("driver with phone: %s is not found.",phone));

    }

    public ResponseEntity<DriverDto> getByEmail(String email) throws DriverNotFoundException {
        Optional<Driver> driver_opt = driverRepository.findByEmail(email);
        if(driver_opt.isPresent()){
            return new ResponseEntity<>(driverMapper
                    .entityToDto(driver_opt.get()),HttpStatus.OK);
        }
        throw new DriverNotFoundException(String
                .format("driver with email: %s is not found.", email));
    }

    public HttpStatus deleteById(Long id) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findById(id);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }else
            throw new DriverNotFoundException(String
                    .format("driver with id: %s is not found.",id));
    }

    private boolean checkDriverEmailExist(String email){
        return driverRepository.findByEmail(email).isPresent();
    }

    private void checkDriverParamsExist(String email, String phone)
            throws DriverAlreadyExistException {
        if(checkDriverEmailExist(email))
            throw new DriverAlreadyExistException(String
                    .format("driver with email: %s is present.",email));

        if(checkDriverPhoneExist(phone))
            throw new DriverAlreadyExistException(String
                    .format("driver with phone: %s is present.",phone));
    }

    public HttpStatus deleteByEmail(String email) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByEmail(email);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }
        throw new DriverNotFoundException(String.format("driver with name: %s is not found.",email));
    }

    public ResponseEntity<DriverDto> getById(Long id) throws DriverNotFoundException{
        if(driverRepository.findById(id).isPresent())
            return new ResponseEntity<>(driverMapper.entityToDto(driverRepository
                    .findById(id).get()),HttpStatus.OK );
        else
            throw new DriverNotFoundException(String
                    .format("driver with id: %s is not found.",id));
    }

    public ResponseEntity<DriverDto> getByPhone(String phone) throws DriverNotFoundException{
        if(driverRepository.findByPhone(phone).isPresent())
            return new ResponseEntity<>(driverMapper.entityToDto(driverRepository
                    .findByPhone(phone).get()),HttpStatus.OK );
        else
            throw new DriverNotFoundException(String
                    .format("driver with phone: %s is not found.",phone));
    }

    public HttpStatus update(DriverDto driverDto) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByPhone(driverDto.getPhone());

        if(driver_opt.isPresent()){
            Driver driver_db = driver_opt.get();

            if(Objects.nonNull(driverDto.getName()) && !driverDto.getName().isEmpty()){
                driver_db.setName(driverDto.getName());
            }

            if(Objects.nonNull(driverDto.getSurname()) && !driverDto.getSurname().isEmpty()){
                driver_db.setSurname(driverDto.getSurname());
            }

            if(Objects.nonNull(driverDto.getPhone()) && !driverDto.getPhone().isEmpty()){
                driver_db.setPhone(driverDto.getPhone());
            }

            return HttpStatus.OK;
        }else
            throw new DriverNotFoundException(String
                    .format("driver with phone: %s is not found", driverDto.getPhone()));
    }

    public HttpStatus addRatingByPhone(int rating, String phone)
            throws DriverNotFoundException, RatingException{
        return addRating(
                rating,
                phone,
                String.format("driver with phone: %s is not found.",phone),
                driverRepository::findByPhone
        );
    }

    public HttpStatus addRatingByEmail(int rating, String email)
            throws DriverNotFoundException, RatingException {
        return addRating(
                rating,
                email,
                String.format("driver with email: %s is not found.", email),
                driverRepository::findByEmail
        );
    }

    private <T> HttpStatus  addRating(int rating,
                                      T param,
                                      String exMessage,
                                      Function<T,Optional<Driver>> repositoryFunc)
            throws DriverNotFoundException , RatingException {
        if (rating > 5 || rating < 0)
            throw new RatingException("invalid rating.");

        Optional<Driver> driver_opt = repositoryFunc.apply(param);

        if (driver_opt.isPresent()) {
            Driver passenger = driver_opt.get();
            driverRepository.save(
                    passenger.setAverageRating(
                            (passenger.getAverageRating() * passenger.getRatingsCount() + rating) /
                                    (passenger.getRatingsCount() + 1))
                    .setRatingsCount(passenger.getRatingsCount() + 1)
            );
            return HttpStatus.OK;
        }

        throw new DriverNotFoundException(exMessage);
    }
    private boolean checkDriverPhoneExist(String phone){
        return driverRepository.findByPhone(phone).isPresent();
    }

    public ResponseEntity<List<DriverDto>> getSortedList(String type) throws SortTypeException {
        List<Driver> drivers = switch (type.toLowerCase()) {
            case "name" -> driverRepository.findAll(Sort.by(Sort.Order.asc("name")));
            case "surname" -> driverRepository.findAll(Sort.by(Sort.Order.asc("surname")));
            default -> throw new SortTypeException("Invalid type of sort");
        };
        return new ResponseEntity<>(drivers.stream()
                .map(driverMapper::entityToDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}

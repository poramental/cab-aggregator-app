package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.entities.Auto;
import com.modsen.driverservice.entities.Driver;
import com.modsen.driverservice.exceptions.*;
import com.modsen.driverservice.mappers.AutoMapper;
import com.modsen.driverservice.mappers.DriverMapper;
import com.modsen.driverservice.repositories.AutoRepository;
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

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;

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

    private void checkDriverEmailExist(String email) throws DriverAlreadyExistException{
        checkDriverParamExist(
                email,
                driverRepository::findByEmail,
                String.format("driver with email: %s is present.",email)
        );
    }

    private void checkDriverParamsExist(String email, String phone)
            throws DriverAlreadyExistException {
        checkDriverEmailExist(email);
        checkDriverPhoneExist(phone);
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

    public HttpStatus update(Long id, DriverDto driverDto)
            throws DriverNotFoundException, DriverAlreadyExistException{
        preUpdateEmailCheck(id, driverDto);
        preUpdatePhoneCheck(id, driverDto);
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
    private void checkDriverPhoneExist(String phone)
            throws DriverAlreadyExistException{
        checkDriverParamExist(
                phone,
                driverRepository::findByEmail,
                String.format("driver with phone: %s is present.",phone)
        );
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


    public <T> void checkDriverParamExist(T param,
                                          Function<T, Optional<Driver>> repositoryFunc,
                                          String exMessage)
            throws DriverAlreadyExistException{
        Optional<Driver> opt_driver = repositoryFunc.apply(param);
        if (opt_driver.isPresent()) {
            throw new DriverAlreadyExistException(exMessage);
        }
    }

    private void preUpdateEmailCheck(Long id, DriverDto driverDto)
            throws DriverAlreadyExistException, DriverNotFoundException {
        Optional<Driver> opt_driver = driverRepository.findById(id);
        if(opt_driver.isPresent()) {
            if (!opt_driver.get().getEmail().equals(driverDto.getEmail()))
                checkDriverEmailExist(driverDto.getEmail());
        }
        else
            throw new DriverNotFoundException(String
                    .format("driver with id : %s is not found.",id));
    }

    public void preUpdatePhoneCheck(Long id,DriverDto driverDto)
            throws DriverNotFoundException,
            DriverAlreadyExistException {
        Optional<Driver> opt_driver = driverRepository.findById(id);
        if (opt_driver.isPresent()){
            if (!opt_driver.get().getPhone().equals(driverDto.getPhone()))
                checkDriverPhoneExist(driverDto.getPhone());
        }
        else
            throw new DriverNotFoundException(String
                    .format("driver with id: %s is not found.",id));
    }

    public HttpStatus addAutoById(Long auto_id, Long driver_id)
            throws AutoNotFoundException,
            DriverAlreadyHaveAutoException,
            DriverNotFoundException {
            return addAuto(
                    driver_id,
                    auto_id,
                    driverRepository::findById,
                    autoRepository::findById,
                    String.format("driver with id: %s is not found",driver_id),
                    String.format("auto with id: %s is not found",auto_id)
            );
    }

    private <T,V> HttpStatus addAuto(T driverParam,
                                     V autoParam,
                                     Function<T,Optional<Driver>> driverRepositoryFunc,
                                     Function<V,Optional<Auto>> autoRepositoryFunc,
                                     String driverExceptionMessage,
                                     String autoExceptionMessage
                                     )
            throws AutoNotFoundException,
            DriverAlreadyHaveAutoException,
            DriverNotFoundException {
        Optional<Driver> driver_opt = driverRepositoryFunc.apply(driverParam);
        Optional<Auto> auto_opt = autoRepositoryFunc.apply(autoParam);
        if(auto_opt.isEmpty()) throw new AutoNotFoundException(autoExceptionMessage);
        if(driver_opt.isPresent()){
            if(driver_opt.get().getAuto() != null)
                throw new DriverAlreadyHaveAutoException("driver already have auto.");
            else{
                driver_opt.get().setAuto(auto_opt.get());
                return HttpStatus.OK;
            }
        }else
            throw new DriverNotFoundException(driverExceptionMessage);

    }


    public HttpStatus addAutoByPhoneAndNumber(String phone, String number)
            throws AutoNotFoundException,
            DriverAlreadyHaveAutoException,
            DriverNotFoundException {
        return addAuto(
                phone,
                number,
                driverRepository::findByPhone,
                autoRepository::findByNumber,
                String.format("driver with phone: %s is not found.",phone),
                String.format("auto with number: %s is not found.",number)
        );
    }

    public HttpStatus setAutoById(Long driver_id, AutoDto autoDto)
            throws DriverAlreadyHaveAutoException,
            DriverNotFoundException {
        return setAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format("driver with id: %s is not found.",driver_id)
        );
    }

    public HttpStatus setAutoByPhone(String phone, AutoDto autoDto)
      throws DriverAlreadyHaveAutoException,
                DriverNotFoundException {
            return setAuto(
                    phone,
                    autoDto,
                    driverRepository::findByPhone,
                    String.format("driver with phone: %s is not found.", phone)
            );
    }

    public HttpStatus setAutoByEmail(String email, AutoDto autoDto) throws DriverAlreadyHaveAutoException,
            DriverNotFoundException {
        return setAuto(
                email,
                autoDto,
                driverRepository::findByEmail,
                String.format("driver with email: %s is not found.", email)
        );
    }

    private <T> HttpStatus setAuto(T param,
                                   AutoDto autoDto,
                                   Function<T, Optional<Driver>>repositoryFunc,
                                   String exceptionMessage
    ) throws DriverAlreadyHaveAutoException,
            DriverNotFoundException {
        Optional<Driver> driver_opt = repositoryFunc.apply(param);
        if(driver_opt.isPresent()){
            if(driver_opt.get().getAuto() != null)
                throw new DriverAlreadyHaveAutoException("driver already have auto.");
            else{
                driver_opt.get().setAuto(autoMapper.dtoToEntity(autoDto));
                return HttpStatus.OK;
            }
        }else throw new DriverNotFoundException(exceptionMessage);
    }

    public HttpStatus replaceAutoById(Long driver_id, AutoDto autoDto)
            throws DriverNotFoundException {
        return replaceAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format("driver with id: %s is not found.",driver_id)
        );
    }
    public HttpStatus replaceAutoByPhone(String phone, AutoDto autoDto)
            throws DriverNotFoundException {
        return replaceAuto(
                phone,
                autoDto,
                driverRepository::findByPhone,
                String.format("driver with phone: %s is not found.",phone)
        );
    }

    public HttpStatus replaceAutoByEmail(String email, AutoDto autoDto)
            throws DriverNotFoundException {
        return replaceAuto(
                email,
                autoDto,
                driverRepository::findByEmail,
                String.format("driver with phone: %s is not found.",email)
        );
    }

    private <T> HttpStatus replaceAuto(
            T param,
            AutoDto autoDto,
            Function<T,Optional<Driver>> driverRepositoryFunc,
            String exceptionMessage
    ) throws DriverNotFoundException {
        Optional<Driver> driver_opt = driverRepositoryFunc.apply(param);
        if(driver_opt.isPresent()){
            driver_opt.get().setAuto(autoMapper.dtoToEntity(autoDto));
            return HttpStatus.OK;
        }else throw new DriverNotFoundException(exceptionMessage);
    }

}

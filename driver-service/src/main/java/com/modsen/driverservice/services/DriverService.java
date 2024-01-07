package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.DriverReqDto;
import com.modsen.driverservice.dto.DriverRespDto;
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

    public ResponseEntity<List<DriverRespDto>> getAll(){
        return new ResponseEntity<>(driverRepository.findAll().stream()
                .map(driverMapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    public HttpStatus add(DriverReqDto driverDto) throws DriverAlreadyExistException {
        checkDriverParamsExist(driverDto.getEmail(),driverDto.getPhone());
        driverRepository.save(driverMapper.reqDtoToEntity(driverDto));
        return HttpStatus.OK;

    }

    public HttpStatus deleteByPhone(String phone) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByPhone(phone);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }throw new DriverNotFoundException(String
                    .format("driver with phone: %s is not found.",phone));

    }

    public ResponseEntity<DriverRespDto> getByEmail(String email) throws DriverNotFoundException {
        Optional<Driver> driver_opt = driverRepository.findByEmail(email);
        if(driver_opt.isPresent()){
            return new ResponseEntity<>(driverMapper
                    .entityToRespDto(driver_opt.get()),HttpStatus.OK);
        }throw new DriverNotFoundException(String
                .format("driver with email: %s is not found.", email));
    }

    public HttpStatus deleteById(Long id) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findById(id);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }throw new DriverNotFoundException(String
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

    public ResponseEntity<DriverRespDto> getById(Long id) throws DriverNotFoundException{
        if(driverRepository.findById(id).isPresent())
            return new ResponseEntity<>(driverMapper.entityToRespDto(driverRepository
                    .findById(id).get()),HttpStatus.OK );
        throw new DriverNotFoundException(String
                    .format("driver with id: %s is not found.",id));
    }

    public ResponseEntity<DriverRespDto> getByPhone(String phone) throws DriverNotFoundException{
        if(driverRepository.findByPhone(phone).isPresent())
            return new ResponseEntity<>(driverMapper.entityToRespDto(driverRepository
                    .findByPhone(phone).get()),HttpStatus.OK );
        throw new DriverNotFoundException(String
                    .format("driver with phone: %s is not found.",phone));
    }

    public HttpStatus update(Long id, DriverReqDto driverDto)
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
        }throw new DriverNotFoundException(String
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
        }throw new DriverNotFoundException(exMessage);
    }
    private void checkDriverPhoneExist(String phone)
            throws DriverAlreadyExistException{
        checkDriverParamExist(
                phone,
                driverRepository::findByPhone,
                String.format("driver with phone: %s is present.",phone)
        );
    }

    public ResponseEntity<List<DriverRespDto>> getSortedList(String type) throws SortTypeException {
        List<Driver> drivers = switch (type.toLowerCase()) {
            case "name" -> driverRepository.findAll(Sort.by(Sort.Order.asc("name")));
            case "surname" -> driverRepository.findAll(Sort.by(Sort.Order.asc("surname")));
            default -> throw new SortTypeException("Invalid type of sort");
        };
        return new ResponseEntity<>(drivers.stream()
                .map(driverMapper::entityToRespDto)
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

    private void preUpdateEmailCheck(Long id, DriverReqDto driverDto)
            throws DriverAlreadyExistException,
            DriverNotFoundException {
        Optional<Driver> opt_driver = driverRepository.findById(id);
        if(opt_driver.isPresent()) {
            if (!opt_driver.get().getEmail().equals(driverDto.getEmail()))
                checkDriverEmailExist(driverDto.getEmail());
        }
        else
            throw new DriverNotFoundException(String
                    .format("driver with id : %s is not found.",id));
    }

    public void preUpdatePhoneCheck(Long id, DriverReqDto driverDto)
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



    public HttpStatus setAutoById(Long driver_id, AutoDto autoDto)
            throws DriverAlreadyHaveAutoException,
            DriverNotFoundException,
            AutoAlreadyExistException {
        return setAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format("driver with id: %s is not found.",driver_id)
        );
    }

    public HttpStatus setAutoByPhone(String phone, AutoDto autoDto)
      throws DriverAlreadyHaveAutoException,
                DriverNotFoundException,

            AutoAlreadyExistException {
            return setAuto(
                    phone,
                    autoDto,
                    driverRepository::findByPhone,
                    String.format("driver with phone: %s is not found.", phone)
            );
    }

    public HttpStatus setAutoByEmail(String email, AutoDto autoDto)
            throws DriverAlreadyHaveAutoException,
            DriverNotFoundException,
            AutoAlreadyExistException {
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
            DriverNotFoundException,
            AutoAlreadyExistException {

        HttpStatus result;
        Optional<Driver> driver_opt = repositoryFunc.apply(param);
        Optional<Auto> auto_opt = autoRepository.findByNumber(autoDto.getNumber());
        if(auto_opt.isPresent()) {
            throw new AutoAlreadyExistException(String
                    .format("auto with number: %s already exist.", auto_opt.get().getNumber()));
        }
        if (driver_opt.isPresent()) {
            if (!driver_opt.get().getAutos().isEmpty())
                throw new DriverAlreadyHaveAutoException("driver already have auto.");
            else {
                driver_opt.get().getAutos().add(autoMapper.dtoToEntity(autoDto));
                driverRepository.save(driver_opt.get());
                result = HttpStatus.OK;
            }
        } else throw new DriverNotFoundException(exceptionMessage);
        return result;
    }

    public HttpStatus replaceAutoById(Long driver_id, AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
        return replaceAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format("driver with id: %s is not found.",driver_id)
        );
    }
    public HttpStatus replaceAutoByPhone(String phone, AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException{
        return replaceAuto(
                phone,
                autoDto,
                driverRepository::findByPhone,
                String.format("driver with phone: %s is not found.",phone)
        );
    }

    public HttpStatus replaceAutoByEmail(String email, AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
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
    ) throws DriverNotFoundException,
            AutoAlreadyExistException{
        HttpStatus result;
        Optional<Driver> driver_opt = driverRepositoryFunc.apply(param);
        Optional<Auto> auto_opt = autoRepository.findByNumber(autoDto.getNumber());
        if(auto_opt.isPresent()) {
            throw new AutoAlreadyExistException(String
                    .format("auto with number: %s already exist.", auto_opt.get().getNumber()));
        }
        if (driver_opt.isPresent()) {
            driver_opt.get().getAutos().set(0,autoMapper.dtoToEntity(autoDto));
            driverRepository.save(driver_opt.get());
            result = HttpStatus.OK;
        } else throw new DriverNotFoundException(exceptionMessage);
        return result;
    }

}

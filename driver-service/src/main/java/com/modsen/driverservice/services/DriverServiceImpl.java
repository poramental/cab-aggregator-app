package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.DriverPageResponse;
import com.modsen.driverservice.dto.DriverRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.entities.Driver;
import com.modsen.driverservice.exceptions.*;
import com.modsen.driverservice.mappers.AutoMapper;
import com.modsen.driverservice.mappers.DriverMapper;
import com.modsen.driverservice.repositories.AutoRepository;
import com.modsen.driverservice.repositories.DriverRepository;
import com.modsen.driverservice.services.interfaces.DriverService;
import com.modsen.driverservice.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {


    private final DriverMapper driverMapper;

    private final DriverRepository driverRepository;

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;

    private final PaginationService paginationService;


    public ResponseEntity<List<DriverResponse>> getAll(){
        return new ResponseEntity<>(driverRepository.findAll().stream()
                .map(driverMapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    public DriverResponse add(DriverRequest driverDto) throws DriverAlreadyExistException {
        checkDriverParamsExist(driverDto.getEmail(),driverDto.getPhone());
        return driverMapper
                .entityToRespDto(driverRepository.save(driverMapper.reqDtoToEntity(driverDto)));
    }


    public DriverResponse deleteById(Long id) throws DriverNotFoundException{
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id)));
        driverRepository.delete(driver);
        return driverMapper.entityToRespDto(driver);

    }

    private void checkDriverEmailExist(String email) throws DriverAlreadyExistException{
        checkDriverParamExist(
                email,
                driverRepository::findByEmail,
                String.format(ExceptionMessage.DRIVER_EMAIL_ALREADY_EXIST_EXCEPTION,email)
        );
    }

    private void checkDriverParamsExist(String email, String phone)
            throws DriverAlreadyExistException {
        checkDriverEmailExist(email);
        checkDriverPhoneExist(phone);
    }

    public DriverResponse getById(Long id) throws DriverNotFoundException{
        return driverMapper.entityToRespDto(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id))));
    }


    public DriverResponse update(Long id, DriverRequest driverDto)
            throws DriverNotFoundException, DriverAlreadyExistException{
        preUpdateAllParamsCheck(driverDto, id);
        Driver driver = driverMapper.reqDtoToEntity(driverDto);
        return driverMapper.entityToRespDto(driverRepository.save(driver));
    }

    public DriverResponse addRatingById(Long id, int rating)
            throws DriverNotFoundException, RatingException {
        return addRating(
                rating,
                id,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id),
                driverRepository::findById
        );
    }

    private <T> DriverResponse addRating(int rating,
                                         T param,
                                         String exMessage,
                                         Function<T,Optional<Driver>> repositoryFunc)
            throws DriverNotFoundException, RatingException {
        if (rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessage.RATING_EXCEPTION);
        }
        Driver driver = repositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exMessage));
        return driverMapper.entityToRespDto(driverRepository.save(
                    driver.setAverageRating(
                            (driver.getAverageRating() * driver.getRatingsCount() + rating) /
                                    (driver.getRatingsCount() + 1))
                    .setRatingsCount(driver.getRatingsCount() + 1)
            ));
    }

    private void checkDriverPhoneExist(String phone)
            throws DriverAlreadyExistException{
        checkDriverParamExist(
                phone,
                driverRepository::findByPhone,
                String.format(ExceptionMessage.DRIVER_PHONE_ALREADY_EXIST_EXCEPTION,phone)
        );
    }

    private void preUpdateAllParamsCheck(DriverRequest driverDto, Long id)
            throws DriverNotFoundException, DriverAlreadyExistException {
        preUpdateEmailCheck(id, driverDto);
        preUpdatePhoneCheck(id, driverDto);
    }

    private <T> void checkDriverParamExist(T param,
                                          Function<T, Optional<Driver>> repositoryFunc,
                                          String exMessage)
            throws DriverAlreadyExistException{
        repositoryFunc.apply(param).orElseThrow(() -> new DriverAlreadyExistException(exMessage));

    }

    private void preUpdateEmailCheck(Long id, DriverRequest driverDto)
            throws DriverAlreadyExistException,
            DriverNotFoundException {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id)));
        if (!driver.getEmail().equals(driverDto.getEmail()))
            checkDriverEmailExist(driverDto.getEmail());

    }

    private void preUpdatePhoneCheck(Long id, DriverRequest driverDto)
            throws DriverNotFoundException,
            DriverAlreadyExistException {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id)));
        if (!driver.getPhone().equals(driverDto.getPhone()))
            checkDriverPhoneExist(driverDto.getPhone());
    }

    public DriverResponse setAutoById(Long driver_id, AutoDto autoDto)
            throws DriverAlreadyHaveAutoException,
            DriverNotFoundException,
            AutoAlreadyExistException {
        return setAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, driver_id)
        );
    }

    private <T> DriverResponse setAuto(T param,
                                       AutoDto autoDto,
                                       Function<T, Optional<Driver>>repositoryFunc,
                                       String exceptionMessage
    ) throws DriverAlreadyHaveAutoException,
            DriverNotFoundException,
            AutoAlreadyExistException {
        Driver driver = repositoryFunc.apply(param).orElseThrow(() -> new DriverNotFoundException(exceptionMessage));
        autoRepository.findByNumber(autoDto.getNumber()).orElseThrow(() -> new AutoAlreadyExistException(String
                .format(ExceptionMessage.AUTO_NUMBER_ALREADY_EXIST_EXCEPTION, autoDto.getNumber())));
        if (!driver.getAutos().isEmpty())
            throw new DriverAlreadyHaveAutoException(ExceptionMessage.DRIVER_ALREADY_HAVE_AUTO_EXCEPTION);
        else {
            driver.getAutos().add(autoMapper.dtoToEntity(autoDto));
            return driverMapper.entityToRespDto(driverRepository.save(driver));
        }
    }

    public DriverResponse replaceAutoById(Long driver_id, AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
        return replaceAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,driver_id)
        );
    }

    private <T> DriverResponse replaceAuto(
            T param,
            AutoDto autoDto,
            Function<T,Optional<Driver>> driverRepositoryFunc,
            String exceptionMessage
    ) throws DriverNotFoundException,
            AutoAlreadyExistException{
        Driver driver = driverRepositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exceptionMessage));
        autoRepository.findByNumber(autoDto.getNumber())
                .orElseThrow(() -> new AutoAlreadyExistException(String
                        .format(ExceptionMessage.AUTO_NUMBER_ALREADY_EXIST_EXCEPTION, autoDto.getNumber())));
        driver.getAutos().set(0,autoMapper.dtoToEntity(autoDto));
        return driverMapper.entityToRespDto(driverRepository.save(driver));
    }

    public DriverPageResponse getDriversPage(int page, int size, String orderBy)
            throws PaginationFormatException {

      Page<Driver> driversPage = paginationService.getPage(
              page,
              size,
              orderBy,
              driverRepository::findAll
      );
        List<Driver> retrievedDrivers = driversPage.getContent();
        long total = driversPage.getTotalElements();

        List<DriverResponse> drivers = retrievedDrivers.stream()
                .map(driverMapper::entityToRespDto)
                .toList();
        return DriverPageResponse.builder()
                .driversList(drivers)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

}

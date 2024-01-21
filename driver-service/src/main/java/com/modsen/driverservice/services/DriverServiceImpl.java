package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.entities.Auto;
import com.modsen.driverservice.entities.Driver;
import com.modsen.driverservice.exceptions.*;
import com.modsen.driverservice.feignclients.RideFeignClient;
import com.modsen.driverservice.mappers.AutoMapper;
import com.modsen.driverservice.mappers.DriverMapper;
import com.modsen.driverservice.repositories.AutoRepository;
import com.modsen.driverservice.repositories.DriverRepository;
import com.modsen.driverservice.services.interfaces.DriverService;
import com.modsen.driverservice.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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

    private final RideFeignClient rideFeignClient;

    public DriverListResponse getAll(){
        return new DriverListResponse(driverRepository.findAll().stream()
                .map(driverMapper::entityToResp)
                .collect(Collectors.toList()));
    }


    public DriverResponse add(DriverRequest driverDto) {
        checkDriverParamsExist(driverDto.getEmail(),driverDto.getPhone());
        return driverMapper
                .entityToResp(driverRepository.save(driverMapper.reqToEntity(driverDto).setIsInRide(false)));
    }


    public DriverResponse deleteById(Long id){
        Driver driver = getDriverOrThrow(id);
        driverRepository.delete(driver);
        return driverMapper.entityToResp(driver);

    }

    private void checkDriverEmailExist(String email){
        checkDriverParamExist(
                email,
                driverRepository::existsByEmail,
                String.format(ExceptionMessage.DRIVER_EMAIL_ALREADY_EXIST_EXCEPTION,email)
        );
    }

    private void checkDriverParamsExist(String email, String phone)
    {
        checkDriverEmailExist(email);
        checkDriverPhoneExist(phone);
    }

    public DriverResponse getById(Long id){
        return driverMapper.entityToResp(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(
                        ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,
                        id))));
    }


    public DriverResponse update(Long id, DriverRequest driverDto)
    {
        Driver oldDriver = getDriverOrThrow(id);
        preUpdateAllParamsCheck(driverDto, id);
        Driver newDriver = driverMapper.reqToEntity(driverDto);
        newDriver.setId(oldDriver.getId());
        return driverMapper.entityToResp(driverRepository
                .save(newDriver.setAutos(oldDriver.getAutos())));
    }

    public DriverResponse addRatingById(Long id, UUID rideId, int rating)
    {
        return addRating(
                rating,
                id,
                rideId,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id),
                driverRepository::findById
        );
    }

    private <T> DriverResponse addRating(int rating,
                                         T param,
                                         UUID rideId,
                                         String exMessage,
                                         Function<T,Optional<Driver>> repositoryFunc)
    {
        if (rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessage.RATING_EXCEPTION);
        }
        Driver driver = repositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exMessage));

        RideResponse rideResponse = rideFeignClient.getRideById(rideId);
        if (!Objects.equals(rideResponse.getDriverId(), driver.getId())) {
            throw new RideHaveAnotherDriverException(ExceptionMessage.RIDE_HAVE_ANOTHER_DRIVER);
        }

        if(Objects.isNull(rideResponse.getEndDate())){
            throw new RideIsNotInactiveException(ExceptionMessage.RIDE_IS_NOT_INACTIVE_EXCEPTION);
        }

        float ratingSum = driver.getAverageRating() * driver.getRatingsCount();
        int newRatingsCount = driver.getRatingsCount() + 1;
        return driverMapper.entityToResp(driverRepository.save(
                    driver.setAverageRating((ratingSum + rating) / newRatingsCount)
                    .setRatingsCount(newRatingsCount)
            ));
    }

    private void checkDriverPhoneExist(String phone)
    {
        checkDriverParamExist(
                phone,
                driverRepository::existsByPhone,
                String.format(ExceptionMessage.DRIVER_PHONE_ALREADY_EXIST_EXCEPTION,phone)
        );
    }

    private void preUpdateAllParamsCheck(DriverRequest driverDto, Long id)
    {
        preUpdateEmailCheck(id, driverDto);
        preUpdatePhoneCheck(id, driverDto);
    }

    private void checkDriverParamExist(String param,
                                          Function<String, Boolean> repositoryFunc,
                                          String exMessage)
    {
        if(repositoryFunc.apply(param)){
            throw new DriverAlreadyExistException(exMessage);
        }
    }

    private void preUpdateEmailCheck(Long id, DriverRequest driverDto)
    {
        Driver driver = getDriverOrThrow(id);
        if (!driver.getEmail().equals(driverDto.getEmail()))
            checkDriverEmailExist(driverDto.getEmail());

    }

    private void preUpdatePhoneCheck(Long id, DriverRequest driverDto)
    {
        Driver driver = getDriverOrThrow(id);
        if (!driver.getPhone().equals(driverDto.getPhone()))
            checkDriverPhoneExist(driverDto.getPhone());
    }

    public DriverResponse setAutoById(Long driver_id, AutoRequest autoDto)
    {
        return setAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, driver_id)
        );
    }

    //метод ставит машину водителю если машина и водитель свободны
    private <T> DriverResponse setAuto(T param,
                                       AutoRequest autoDto,
                                       Function<T, Optional<Driver>>repositoryFunc,
                                       String exceptionMessage)
    {
        Driver driver = repositoryFunc.apply(param).orElseThrow(() -> new DriverNotFoundException(exceptionMessage));

        if(autoRepository.existsByNumber(autoDto.getNumber())){
            throw new AutoAlreadyExistException(String.format(
                    ExceptionMessage.AUTO_NUMBER_ALREADY_EXIST_EXCEPTION,
                    autoDto.getNumber()));
        }

        if (!driver.getAutos().isEmpty())
            throw new DriverAlreadyHaveAutoException(ExceptionMessage.DRIVER_ALREADY_HAVE_AUTO_EXCEPTION);
        else {
            driver.getAutos().add(autoMapper.dtoToEntity(autoDto));
            return driverMapper.entityToResp(driverRepository.save(driver));
        }
    }

    public DriverResponse replaceAutoById(Long driver_id, AutoRequest autoDto)
    {
        return replaceAuto(
                driver_id,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,driver_id)
        );
    }

    //метод ставит новую машину если такой нет, и старую если находит машину в базе по номеру
    private <T> DriverResponse replaceAuto(
            T param,
            AutoRequest autoDto,
            Function<T,Optional<Driver>> driverRepositoryFunc,
            String exceptionMessage)
    {
        Optional<Auto> autoOpt = autoRepository.findByNumber(autoDto.getNumber());

        Driver driver = driverRepositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exceptionMessage));
        driver.getAutos().get(0).setDriverId(null);
        driver.getAutos().clear();
        if(autoOpt.isPresent()){
            Auto oldAuto = autoOpt.get();
            Auto newAuto = autoMapper.dtoToEntity(autoDto).setId(oldAuto.getId());
            driver.getAutos().add(autoRepository.save(newAuto.setDriverId(driver.getId())));
        }else {
            driver.getAutos().add(autoMapper.dtoToEntity(autoDto).setDriverId(driver.getId()));
        }

        return driverMapper.entityToResp(driverRepository.save(driver));
    }

    public DriverPageResponse getDriversPage(int page, int size, String orderBy)
    {
      Page<Driver> driversPage = paginationService.getPage(
              page,
              size,
              orderBy,
              driverRepository::findAll
      );
        List<Driver> retrievedDrivers = driversPage.getContent();
        long total = driversPage.getTotalElements();
        List<DriverResponse> drivers = retrievedDrivers.stream()
                .map(driverMapper::entityToResp)
                .toList();
        return DriverPageResponse.builder()
                .driversList(drivers)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

    private Driver getDriverOrThrow(Long id){
        return driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,id)));
    }

    public DriverResponse changeIsInRideStatus(Long driverId) {
        Driver driver = getDriverOrThrow(driverId);
        return driverMapper.entityToResp(driverRepository.save(driver.setIsInRide(!driver.getIsInRide())));
    }
}

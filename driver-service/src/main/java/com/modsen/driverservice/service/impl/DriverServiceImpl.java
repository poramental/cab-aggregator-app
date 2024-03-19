package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.entity.Auto;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exception.*;
import com.modsen.driverservice.feignclient.RideFeignClient;
import com.modsen.driverservice.kafka.DriverProducer;
import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.service.RideService;
import com.modsen.driverservice.util.ExceptionMessages;
import com.modsen.driverservice.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {


    private final DriverMapper driverMapper;

    private final DriverRepository driverRepository;

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;

    private final RideService rideService;

    private final DriverProducer driverProducer;

    @Override
    public ListDriverResponse getAll() {
        return new ListDriverResponse(driverRepository.findAll().stream()
                .map(driverMapper::entityToResp)
                .toList());
    }

    @Override
    public DriverResponse add(DriverRequest driverDto) {
        checkDriverParamsExist(driverDto.getEmail(), driverDto.getPhone());
        return driverMapper
                .entityToResp(driverRepository.save(driverMapper.reqToEntity(driverDto).setIsInRide(false)));
    }

    @Override
    public DriverResponse deleteById(Long id) {
        Driver driver = getDriverOrThrow(id);
        driverRepository.delete(driver);
        return driverMapper.entityToResp(driver);

    }

    private void checkDriverEmailExist(String email) {
        checkDriverParamExist(
                email,
                driverRepository::existsByEmail,
                String.format(ExceptionMessages.DRIVER_EMAIL_ALREADY_EXIST_EXCEPTION, email)
        );
    }

    private void checkDriverParamsExist(String email, String phone) {
        checkDriverEmailExist(email);
        checkDriverPhoneExist(phone);
    }

    @Override
    public DriverResponse getById(Long id) {
        return driverMapper.entityToResp(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(
                        ExceptionMessages.DRIVER_NOT_FOUND_EXCEPTION,
                        id))));
    }

    @Override
    public DriverResponse update(Long id, DriverRequest driverDto) {
        Driver oldDriver = getDriverOrThrow(id);
        preUpdateAllParamsCheck(driverDto, id);
        Driver newDriver = driverMapper.reqToEntity(driverDto);
        newDriver
                .setId(id)
                .setIsInRide(oldDriver.getIsInRide())
                .setAverageRating(oldDriver.getAverageRating())
                .setRatingsCount(oldDriver.getRatingsCount());
        return driverMapper.entityToResp(driverRepository
                .save(newDriver.setAutos(oldDriver.getAutos())));
    }

    @Override
    public DriverResponse addRatingById(Long id, UUID rideId, int rating) {
        return addRating(
                rating,
                id,
                rideId,
                String.format(ExceptionMessages.DRIVER_NOT_FOUND_EXCEPTION, id),
                driverRepository::findById
        );
    }

    private <T> DriverResponse addRating(int rating,
                                         T param,
                                         UUID rideId,
                                         String exMessage,
                                         Function<T, Optional<Driver>> repositoryFunc) {
        if (rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessages.RATING_EXCEPTION);
        }
        Driver driver = repositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exMessage));

        RideResponse rideResponse = rideService.getRideById(rideId);
        LocalDateTime rideResponseEndDate = rideResponse.getEndDate();

        if (!rideResponseEndDate.isAfter(LocalDateTime.now().minusMinutes(3))) {
            throw new RatingException(ExceptionMessages.RATING_EXPIRED_EXCEPTION);
        }

        if (!Objects.equals(rideResponse.getDriverId(), driver.getId())) {
            throw new RideHaveAnotherDriverException(ExceptionMessages.RIDE_HAVE_ANOTHER_DRIVER);
        }

        if (Objects.isNull(rideResponse.getEndDate())) {
            throw new RideIsNotInactiveException(ExceptionMessages.RIDE_IS_NOT_INACTIVE_EXCEPTION);
        }

        float ratingSum = driver.getAverageRating() * driver.getRatingsCount();
        int newRatingsCount = driver.getRatingsCount() + 1;
        return driverMapper.entityToResp(driverRepository.save(
                driver.setAverageRating((ratingSum + rating) / newRatingsCount)
                        .setRatingsCount(newRatingsCount)
        ));
    }

    private void checkDriverPhoneExist(String phone) {
        checkDriverParamExist(
                phone,
                driverRepository::existsByPhone,
                String.format(ExceptionMessages.DRIVER_PHONE_ALREADY_EXIST_EXCEPTION, phone)
        );
    }

    private void preUpdateAllParamsCheck(DriverRequest driverDto, Long id) {
        preUpdateEmailCheck(id, driverDto);
        preUpdatePhoneCheck(id, driverDto);
    }

    private void checkDriverParamExist(String param,
                                       Function<String, Boolean> repositoryFunc,
                                       String exMessage) {
        if (repositoryFunc.apply(param)) {
            throw new DriverAlreadyExistException(exMessage);
        }
    }

    private void preUpdateEmailCheck(Long id, DriverRequest driverDto) {
        Driver driver = getDriverOrThrow(id);
        if (!driver.getEmail().equals(driverDto.getEmail()))
            checkDriverEmailExist(driverDto.getEmail());

    }

    private void preUpdatePhoneCheck(Long id, DriverRequest driverDto) {
        Driver driver = getDriverOrThrow(id);
        if (!driver.getPhone().equals(driverDto.getPhone()))
            checkDriverPhoneExist(driverDto.getPhone());
    }

    @Override
    public DriverResponse setAutoById(Long driverId, AutoRequest autoDto) {
        return setAuto(
                driverId,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessages.DRIVER_NOT_FOUND_EXCEPTION, driverId)
        );
    }

    //метод ставит машину водителю если машина и водитель свободны
    private <T> DriverResponse setAuto(T param,
                                       AutoRequest autoDto,
                                       Function<T, Optional<Driver>> repositoryFunc,
                                       String exceptionMessage) {
        Driver driver = repositoryFunc.apply(param).orElseThrow(() -> new DriverNotFoundException(exceptionMessage));

        if (autoRepository.existsByNumber(autoDto.getNumber())) {
            throw new AutoAlreadyExistException(String.format(
                    ExceptionMessages.AUTO_NUMBER_ALREADY_EXIST_EXCEPTION,
                    autoDto.getNumber()));
        }

        if (!driver.getAutos().isEmpty())
            throw new DriverAlreadyHaveAutoException(ExceptionMessages.DRIVER_ALREADY_HAVE_AUTO_EXCEPTION);
        else {
            driver.getAutos().add(autoMapper.dtoToEntity(autoDto).setDriverId(driver.getId()));
            return driverMapper.entityToResp(driverRepository.save(driver));
        }
    }

    @Override
    public DriverResponse replaceAutoById(Long driverId, AutoRequest autoDto) {
        return replaceAuto(
                driverId,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessages.DRIVER_NOT_FOUND_EXCEPTION, driverId)
        );
    }

    //метод ставит новую машину если такой нет, и старую если находит машину в базе по номеру
    private <T> DriverResponse replaceAuto(
            T param,
            AutoRequest autoDto,
            Function<T, Optional<Driver>> driverRepositoryFunc,
            String exceptionMessage) {
        Optional<Auto> autoOpt = autoRepository.findByNumber(autoDto.getNumber());

        Driver driver = driverRepositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exceptionMessage));

        driver.getAutos().clear();
        if (autoOpt.isPresent()) {
            Auto oldAuto = autoOpt.get();
            Auto newAuto = autoMapper.dtoToEntity(autoDto).setId(oldAuto.getId());
            driver.getAutos().add(autoRepository.save(newAuto.setDriverId(driver.getId())));
        } else {
            driver.getAutos().add(autoMapper.dtoToEntity(autoDto).setDriverId(driver.getId()));
        }

        return driverMapper.entityToResp(driverRepository.save(driver));
    }

    @Override
    public DriverPageResponse getDriversPage(int page, int size, String orderBy) {
        Page<Driver> driversPage = PaginationUtil.getPage(
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

    private Driver getDriverOrThrow(Long id) {
        return driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessages.DRIVER_NOT_FOUND_EXCEPTION, id)));
    }

    @Override
    public DriverResponse changeIsInRideStatus(Long driverId) {
        Driver driver = getDriverOrThrow(driverId);
        return driverMapper.entityToResp(driverRepository.save(driver.setIsInRide(!driver.getIsInRide())));
    }

    @Override
    public List<DriverResponse> getAvailableDrivers() {
        return driverRepository.findAllByIsInRideIsFalse().stream()
                .map(driverMapper::entityToResp)
                .toList();
    }

    @Override
    public void findDriverForRide(FindDriverRequest request) {
        List<Driver> availableDrivers = driverRepository.findAllByIsInRideIsFalse();
        if ((Objects.nonNull(request.getNotAcceptedDrivers()) && !request.getNotAcceptedDrivers().isEmpty())) {
            availableDrivers = availableDrivers.stream()
                    .filter(x -> !request.getNotAcceptedDrivers().contains(x.getId()))
                    .toList();
        }
        if ((Objects.nonNull(request.getWaitingDrivers()) && !request.getWaitingDrivers().isEmpty())) {
            availableDrivers = availableDrivers.stream()
                    .filter(x -> !request.getWaitingDrivers().contains(x.getId()))
                    .toList();
        }
        if (availableDrivers.isEmpty()) {
            driverProducer.sendMessage(
                    DriverForRideResponse.builder()
                            .driverId(0L)
                            .rideId(request.getRideId())
                            .build()
            );
        } else {

            driverProducer.sendMessage(
                    DriverForRideResponse.builder()
                            .driverId(availableDrivers.get(0).getId())
                            .driverResponse(driverMapper.entityToResp(availableDrivers.get(0)))
                            .rideId(request.getRideId())
                            .build()
            );
        }
    }
}

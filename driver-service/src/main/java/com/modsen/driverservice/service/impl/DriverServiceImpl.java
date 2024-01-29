package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exception.*;
import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.util.ExceptionMessage;
import com.modsen.driverservice.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @Override
    public ListDriverResponse getAll() {
        return new ListDriverResponse(driverRepository.findAll().stream()
                .map(driverMapper::entityToRespDto)
                .collect(Collectors.toList()));
    }

    @Override
    public DriverResponse add(DriverRequest driverDto) {
        checkDriverParamsExist(driverDto.getEmail(), driverDto.getPhone());
        return driverMapper
                .entityToRespDto(driverRepository.save(driverMapper.reqDtoToEntity(driverDto)));
    }

    @Override
    public DriverResponse deleteById(Long id) {
        Driver driver = getDriverOrThrow(id);
        driverRepository.delete(driver);
        return driverMapper.entityToRespDto(driver);

    }

    private void checkDriverEmailExist(String email) {
        checkDriverParamExist(
                email,
                driverRepository::existsByEmail,
                String.format(ExceptionMessage.DRIVER_EMAIL_ALREADY_EXIST_EXCEPTION, email)
        );
    }

    private void checkDriverParamsExist(String email, String phone) {
        checkDriverEmailExist(email);
        checkDriverPhoneExist(phone);
    }
    @Override
    public DriverResponse getById(Long id) {
        return driverMapper.entityToRespDto(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(
                        ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION,
                        id))));
    }

    @Override
    public DriverResponse update(Long id, DriverRequest driverDto) {
        preUpdateAllParamsCheck(driverDto, id);
        Driver driver = driverMapper.reqDtoToEntity(driverDto);
        return driverMapper.entityToRespDto(driverRepository.save(driver));
    }

    @Override
    public DriverResponse addRatingById(Long id, int rating) {
        return addRating(
                rating,
                id,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, id),
                driverRepository::findById
        );
    }

    private <T> DriverResponse addRating(int rating,
                                         T param,
                                         String exMessage,
                                         Function<T, Optional<Driver>> repositoryFunc) {
        if (rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessage.RATING_EXCEPTION);
        }
        Driver driver = repositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exMessage));
        float ratingSum = driver.getAverageRating() * driver.getRatingsCount();
        int newRatingsCount = driver.getRatingsCount() + 1;
        return driverMapper.entityToRespDto(driverRepository.save(
                driver.setAverageRating((ratingSum + rating) / newRatingsCount)
                        .setRatingsCount(newRatingsCount)
        ));
    }

    private void checkDriverPhoneExist(String phone) {
        checkDriverParamExist(
                phone,
                driverRepository::existsByPhone,
                String.format(ExceptionMessage.DRIVER_PHONE_ALREADY_EXIST_EXCEPTION, phone)
        );
    }

    private void preUpdateAllParamsCheck(DriverRequest driverDto, Long id) {
        preUpdateEmailCheck(id, driverDto);
        preUpdatePhoneCheck(id, driverDto);
    }

    private <T> void checkDriverParamExist(T param,
                                           Function<T, Boolean> repositoryFunc,
                                           String exMessage) {
        if(repositoryFunc.apply(param)) {
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
    public DriverResponse setAutoById(Long driverId, AutoDto autoDto) {
        return setAuto(
                driverId,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, driverId)
        );
    }

    private <T> DriverResponse setAuto(T param,
                                       AutoDto autoDto,
                                       Function<T, Optional<Driver>> repositoryFunc,
                                       String exceptionMessage) {
        Driver driver = repositoryFunc.apply(param).orElseThrow(() -> new DriverNotFoundException(exceptionMessage));
        autoRepository.findByNumber(autoDto.getNumber()).orElseThrow(() -> new AutoNotFoundException(String.format(
                ExceptionMessage.AUTO_NOT_FOUND_EXCEPTION,
                autoDto.getNumber()))
        );
        if (!driver.getAutos().isEmpty())
            throw new DriverAlreadyHaveAutoException(ExceptionMessage.DRIVER_ALREADY_HAVE_AUTO_EXCEPTION);

        return driverMapper.entityToRespDto(driverRepository.save(driver), autoDto);

    }
    @Override
    public DriverResponse replaceAutoById(Long driverId, AutoDto autoDto) {
        return replaceAuto(
                driverId,
                autoDto,
                driverRepository::findById,
                String.format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, driverId)
        );
    }

    private <T> DriverResponse replaceAuto(
            T param,
            AutoDto autoDto,
            Function<T, Optional<Driver>> driverRepositoryFunc,
            String exceptionMessage) {
        Driver driver = driverRepositoryFunc.apply(param)
                .orElseThrow(() -> new DriverNotFoundException(exceptionMessage));
        autoRepository.findByNumber(autoDto.getNumber())
                .orElseThrow(() -> new AutoNotFoundException(String
                        .format(ExceptionMessage.AUTO_NOT_FOUND_EXCEPTION, autoDto.getNumber())));
        driver.getAutos().set(0, autoMapper.dtoToEntity(autoDto));
        return driverMapper.entityToRespDto(driverRepository.save(driver), autoDto);
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
                .map(driverMapper::entityToRespDto)
                .toList();
        return DriverPageResponse.builder()
                .driversList(drivers)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

    private Driver getDriverOrThrow(Long id) {
        return driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(String
                .format(ExceptionMessage.DRIVER_NOT_FOUND_EXCEPTION, id)));
    }

}

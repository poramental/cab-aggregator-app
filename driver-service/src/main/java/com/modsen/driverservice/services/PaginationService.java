package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.exceptions.PaginationFormatException;
import com.modsen.driverservice.util.ExceptionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
public class PaginationService {
    private  PageRequest getPageRequest(int page, int size, String orderBy)
            throws PaginationFormatException {
        if (page < 1 || size < 1) {
            throw new PaginationFormatException(ExceptionMessage.PAGINATION_FORMAT_EXCEPTION);
        }
        PageRequest pageRequest;
        if (orderBy == null) {
            pageRequest = PageRequest.of(page - 1, size);
        } else {
            validateSortingParameter(orderBy);
            pageRequest = PageRequest.of(page - 1, size, Sort.by(orderBy));
        }
        return pageRequest;
    }

    private void validateSortingParameter(String orderBy)
            throws PaginationFormatException {
        List<String> fieldNames = Arrays.stream(DriverResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        if (!fieldNames.contains(orderBy)) {
            throw new PaginationFormatException(ExceptionMessage.INVALID_TYPE_OF_SORT);
        }
    }

    public <T> Page<T> getPage(int page,
                               int size,
                               String orderBy,
                               Function<PageRequest,Page<T>> repositoryFindAll)
            throws PaginationFormatException {
        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        return repositoryFindAll.apply(pageRequest);
    }
}

package com.modsen.driverservice.util;

import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.exception.PaginationFormatException;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;

@UtilityClass
public class PaginationUtil {
    private static PageRequest getPageRequest(int page, int size, String orderBy) {
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

    private static void validateSortingParameter(String orderBy) {
        Arrays.stream(DriverResponse.class.getDeclaredFields())
                .map(Field::getName)
                .filter(orderBy::equals).toList().stream().findFirst()
                .orElseThrow(() -> new PaginationFormatException(ExceptionMessage.INVALID_TYPE_OF_SORT));

    }

    public static <T> Page<T> getPage(int page,
                                      int size,
                                      String orderBy,
                                      Function<PageRequest, Page<T>> repositoryFindAll)
            throws PaginationFormatException {
        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        return repositoryFindAll.apply(pageRequest);
    }
}

package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import ru.alexandr.BookingCinemaTickets.application.dto.ApiErrorDto;

public class ApiErrorDtoAssert extends AbstractAssert<ApiErrorDtoAssert, ApiErrorDto> {
    protected ApiErrorDtoAssert(ApiErrorDto apiErrorDto) {
        super(apiErrorDto, ApiErrorDtoAssert.class);
    }

    public static ApiErrorDtoAssert assertThat(ApiErrorDto actual) {
        return new ApiErrorDtoAssert(actual);
    }

    public ApiErrorDtoAssert exceptionNameIsEqualTo(String expected) {
        Assertions.assertThat(actual.exceptionName()).isEqualTo(expected);
        return this;
    }

    public ApiErrorDtoAssert stackTraceIsNotEmpty() {
        Assertions.assertThat(actual.stackTrace()).isNotEmpty();
        return this;
    }

    public ApiErrorDtoAssert statusCodeIsEqualTo(Integer expected) {
        Assertions.assertThat(actual.statusCode()).isEqualTo(expected);
        return this;
    }

    public ApiErrorDtoAssert pathIsEqualTo(String expected) {
        Assertions.assertThat(actual.path()).isEqualTo(expected);
        return this;
    }

}

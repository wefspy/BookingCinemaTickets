package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.util.Objects;

public class RoleDtoAssert extends AbstractAssert<RoleDtoAssert, RoleDto> {
    protected RoleDtoAssert(RoleDto roleDto) {
        super(roleDto, RoleDtoAssert.class);
    }

    public static RoleDtoAssert assertThat(RoleDto actual) {
        return new RoleDtoAssert(actual);
    }

    public RoleDtoAssert isEqualTo(Role excepted) {
        isNotNull();

        if (!idIsEqualTo(excepted.getId())) {
            throw failureWithActualExpected(actual, excepted,
                    "Id ролей не совпадают. Ожидалось: %s Текущее: %s", excepted.getId(), actual.id());
        }

        if (!nameIsEqualTo(excepted.getName())) {
            throw failureWithActualExpected(actual, excepted,
                    "Названия ролей не совпадают. Ожидалось: %s Текущее: %s", excepted.getName(), actual.name());
        }

        return this;
    }

    protected Boolean idIsEqualTo(Long id) {
        return Objects.equals(actual.id(), id);
    }

    protected Boolean nameIsEqualTo(String name) {
        return Objects.equals(actual.name(), name);
    }
}

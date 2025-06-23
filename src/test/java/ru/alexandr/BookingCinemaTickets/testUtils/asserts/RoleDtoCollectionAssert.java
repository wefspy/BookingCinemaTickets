package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.CollectionAssert;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;

import java.util.Collection;
import java.util.List;

public class RoleDtoCollectionAssert extends CollectionAssert<RoleDto> {
    protected RoleDtoCollectionAssert(Collection<? extends RoleDto> roleDtos) {
        super(roleDtos);
    }

    public static RoleDtoCollectionAssert assertThat(Collection<? extends RoleDto> actual) {
        return new RoleDtoCollectionAssert(actual);
    }

    public RoleDtoCollectionAssert isEqualTo(Collection<? extends Role> expected) {
        Assertions.assertThat(actual).hasSameSizeAs(expected);
        assertAllElementsMatch(expected);

        return this;
    }

    public RoleDtoCollectionAssert hasBaseRoles() {
        List<String> baseRoles = List.of(RoleEnum.USER.name());

        assertRoleNamesExactly(baseRoles);

        return this;
    }

    private void assertAllElementsMatch(Collection<? extends Role> expected) {
        actual.forEach(actualDto -> {
            Role expectedRole = findExpectedRole(actualDto, expected);
            assertSingleDtoEquals(actualDto, expectedRole, expected);
        });
    }

    private Role findExpectedRole(RoleDto actualDto, Collection<? extends Role> expected) {
        return expected.stream()
                .filter(role -> role.getId().equals(actualDto.id()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        failureWithActualExpected(actual, expected, "В коллекции роли с id %s не найдено", actualDto.id())
                ));
    }

    private void assertSingleDtoEquals(RoleDto actualDto, Role expectedRole, Collection<? extends Role> expected) {
        try {
            RoleDtoAssert.assertThat(actualDto).isEqualTo(expectedRole);
        } catch (AssertionError e) {
            throw failureWithActualExpected(actual, expected,
                    "При сравнении ожидаемого объекта %s и текущего %s" + System.lineSeparator() + e.getMessage(),
                    expectedRole, actualDto);
        }
    }

    private void assertRoleNamesExactly(Collection<String> expectedRoleNames) {
        Assertions.assertThat(actual)
                .hasSize(expectedRoleNames.size())
                .extracting(RoleDto::name)
                .containsExactlyInAnyOrderElementsOf(expectedRoleNames);
    }

}

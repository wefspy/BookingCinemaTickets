package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.RoleDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.RoleDtoCollectionAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityBuilder;

import java.util.Collection;
import java.util.List;

class RoleMapperTest {
    private final RoleMapper roleMapper = new RoleMapper();

    private Role roleAdmin;
    private Role roleManager;

    @BeforeEach
    void setUp() {
        roleAdmin = TestEntityBuilder.role(1L, "ADMIN");
        roleManager = TestEntityBuilder.role(2L, "MANAGER");
    }

    @Test
    void toDto() {
        RoleDto actualDto = roleMapper.toDto(roleAdmin);

        RoleDtoAssert.assertThat(actualDto).isEqualTo(roleAdmin);
    }

    @Test
    void toDtos() {
        List<Role> roles = List.of(roleAdmin, roleManager);

        Collection<RoleDto> dtos = roleMapper.toDtos(roles);

        RoleDtoCollectionAssert.assertThat(dtos).isEqualTo(roles);
    }
}
package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperTest {
    private final RoleMapper roleMapper = new RoleMapper();

    private Role roleAdmin;
    private Role roleManager;

    @BeforeEach
    void setUp() throws Exception {
        roleAdmin = new Role("ADMIN");
        roleManager = new Role("MANAGER");

        Field roleIdField = Role.class.getDeclaredField("id");
        roleIdField.setAccessible(true);

        roleIdField.set(roleAdmin, 1L);
        roleIdField.set(roleManager, 2L);
    }

    @Test
    void getRoleDto_ShouldReturnCorrectRoleDto() {
        RoleDto dtoActual = roleMapper.getRoleDto(roleAdmin);

        assertThat(dtoActual.id()).isEqualTo(roleAdmin.getId());
        assertThat(dtoActual.name()).isEqualTo(roleAdmin.getName());
    }

    @Test
    void getRoleDtos_ShouldReturnCorrectRoleDtos() {
        Set<Role> roles = Set.of(roleAdmin, roleManager);

        Collection<RoleDto> dtos = roleMapper.getRoleDtos(roles);

        assertThat(dtos)
                .hasSize(roles.size())
                .extracting(RoleDto::id)
                .containsAll(
                        roles.stream()
                                .map(Role::getId)
                                .toList()
                );
    }
}
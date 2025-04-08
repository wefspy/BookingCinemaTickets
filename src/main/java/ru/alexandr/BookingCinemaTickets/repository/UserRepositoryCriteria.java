package ru.alexandr.BookingCinemaTickets.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;

@Repository
public class UserRepositoryCriteria implements UserRepositoryCustom {

    private final EntityManager entityManager;

    public UserRepositoryCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findUserByRoleName(String roleName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> user = criteriaQuery.from(User.class);
        Join<User, RoleUser> roleUser = user.join("roleUser");
        Join<RoleUser, Role> role = roleUser.join("role");

        criteriaQuery.select(user)
                .where(criteriaBuilder.equal(role.get("name"), roleName));

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

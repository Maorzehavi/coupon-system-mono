package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.stream.DoubleStream;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getByRole(String role);
    Optional<Set<Role>> getAllByRoleIn(Set<String> roles);
    Boolean existsByRole(String role);
    @Query("select r.id from Role r where r.role = ?1")
    Optional<Long> getIdByRole(String role);
    @Query("select r.role from Role r where r.id = ?1")
    Optional<String> getRoleById(Long id);
    @Query("select r from Role r where r.clientType = ?1")
    Optional<Set<Role>>findAllByClientType(ClientType clientType);

    Optional<Role> findByRole(String role);
}
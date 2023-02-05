package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.RoleRequest;
import com.maorzehavi.couponSystem.model.dto.response.RoleResponse;
import com.maorzehavi.couponSystem.model.entity.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Boolean existsByRole(String role);

    Optional<Long> getIdByRole(String role);

    Optional<RoleResponse> getRole(Long id);
    Optional<RoleResponse> getRole(String role);

    Optional<Set<RoleResponse>> getAllRoles();

    Optional<RoleResponse> createRole(RoleRequest roleRequest);

    Optional<RoleResponse> updateRole(Long id, RoleRequest roleRequest);

    Optional<RoleResponse> deleteRole(Long id);

    Optional<Set<Role>> getAllRolesEntityByClientType(ClientType clientType);

    RoleResponse mapToRoleResponse(RoleRequest roleRequest);
    RoleResponse mapToRoleResponse(Role role);

    Role mapToRole(RoleRequest roleRequest);

    Role mapToRole(RoleResponse roleResponse);

}

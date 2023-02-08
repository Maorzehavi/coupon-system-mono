package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.model.dto.response.UserResponse;
import com.maorzehavi.couponSystem.model.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<Long> getIdByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<UserResponse> getByEmail(String email);

    @Transactional
    @Modifying
    Optional<UserResponse> createUser(UserRequest userRequest, ClientType clientType);

    @Transactional
    @Modifying
    Optional<UserResponse> updateUser(UserRequest userRequest, Principal principal);

    @Transactional
    @Modifying
    Optional<UserResponse> deleteUser(Long id);

    Optional<User> getEntityByEmail(String email);

    List<UserResponse> getAllUsers();

    // mapping methods

    UserResponse mapToUserResponse(User user);

    User mapToUser(UserResponse userResponse);

    User mapToUser(UserRequest userRequest);


}

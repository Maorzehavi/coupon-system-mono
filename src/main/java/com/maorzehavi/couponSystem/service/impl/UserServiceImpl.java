package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.model.dto.response.UserResponse;
import com.maorzehavi.couponSystem.model.entity.User;
import com.maorzehavi.couponSystem.repository.UserRepository;
import com.maorzehavi.couponSystem.service.RoleService;
import com.maorzehavi.couponSystem.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Qualifier("roleServiceImpl")
    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           @Lazy RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserResponse> getByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToUserResponse);
    }

    @Override
    public Optional<UserResponse> createUser(UserRequest userRequest, ClientType clientType) {
        if (existsByEmail(userRequest.getEmail())) {
            throw  new SystemException("Email: " + userRequest.getEmail() + " is taken");
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        var roles = roleService.getAllRolesEntityByClientType(clientType);
        if (roles.isPresent() && roles.get().size() > 0) {
            var user = mapToUser(userRequest);
            user.setClientType(clientType);
            user.setRoles(roles.get());
            return Optional.of(mapToUserResponse(userRepository.save(user)));
        }
        throw new SystemException("Can not find roles for client type " + clientType);
    }

    @Override
    public Optional<UserResponse> updateUser(UserRequest userRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<UserResponse> deleteUser(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
    }

    @Override
    public UserResponse mapToUserResponse(User user) {
        var roles = user.getRoles().stream().map(roleService::mapToRoleResponse).collect(Collectors.toSet());
        return UserResponse.builder()
                .email(user.getEmail())
                .clientType(user.getClientType())
                .roles(roles)
                .build();
    }

    @Override
    public User mapToUser(UserResponse userResponse) {
        var roles = userResponse.getRoles().stream().map(roleService::mapToRole).collect(Collectors.toSet());
        return User.builder()
                .email(userResponse.getEmail())
                .clientType(userResponse.getClientType())
                .roles(roles)
                .build();
    }

    @Override
    public User mapToUser(UserRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
}

package com.maorzehavi.couponSystem.security.service;


import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.model.dto.response.AuthenticationResponse;
import com.maorzehavi.couponSystem.security.user.SecurityUser;
import com.maorzehavi.couponSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(@Valid UserRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userService.getEntityByEmail(request.getEmail()).orElseThrow(() -> new SystemException("User not found"));
        var token = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse register(@Valid UserRequest request, @NonNull ClientType clientType){
        var user = userService.createUser(request, clientType).map(userService::mapToUser).orElseThrow();
        var token = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public void logout(){
        SecurityContextHolder.clearContext();
    }

}

package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.dto.request.AuthorityRequest;
import com.maorzehavi.couponSystem.model.dto.response.AuthorityResponse;
import com.maorzehavi.couponSystem.model.entity.Authority;
import com.maorzehavi.couponSystem.repository.AuthorityRepository;
import com.maorzehavi.couponSystem.service.AuthorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Boolean existsByAuthority(String authority) {
        return authorityRepository.existsByAuthority(authority);
    }

    @Override
    public Optional<Long> getIdByAuthority(String authority) {
        return authorityRepository.getIdByAuthority(authority);
    }

    @Override
    public Optional<AuthorityResponse> getAuthority(Long id) {
        return authorityRepository.findById(id).map(this::mapToAuthorityResponse);
    }

    @Override
    public Optional<Authority> getAuthorityEntity(Long id) {
        return authorityRepository.findById(id);
    }

    @Transactional
    @Override
    public Optional<AuthorityResponse> createAuthority(AuthorityRequest authority) {
        if (authorityRepository.existsByAuthority(authority.getAuthority()))
            throw new SystemException("Authority already exists");
        var a = authorityRepository.save(mapToAuthority(authority));
        return Optional.of(mapToAuthorityResponse(a));

    }

    @Override
    public Optional<AuthorityResponse> updateAuthority(Long id, AuthorityRequest authority) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthorityResponse> deleteAuthority(Long id) {
        var a = authorityRepository.findById(id).orElseThrow(() -> new SystemException("Authority not found"));
        authorityRepository.deleteById(id);
        return Optional.of(mapToAuthorityResponse(a));
    }

    @Override
    public List<AuthorityResponse> getAllAuthorities() {
        return authorityRepository.findAll().stream().map(this::mapToAuthorityResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorityResponse> getByAuthority(String authority) {
        return authorityRepository.getByAuthority(authority).map(this::mapToAuthorityResponse);
    }

    @Override
    public Optional<List<AuthorityResponse>> getAllByAuthorityIn(Set<String> authorities) {
        return authorityRepository.getAllByAuthorityIn(authorities)
                .map(authorities1 -> authorities1.stream()
                        .map(this::mapToAuthorityResponse)
                        .collect(Collectors.toList()));
    }

    @Override
    public AuthorityResponse mapToAuthorityResponse(Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .authority(authority.getAuthority())
                .build();
    }

    @Override
    public AuthorityResponse mapToAuthorityResponse(AuthorityRequest authorityRequest) {
        return AuthorityResponse.builder()
                .authority(authorityRequest.getAuthority())
                .build();
    }

    @Override
    public Authority mapToAuthority(AuthorityResponse authorityResponse) {
        return Authority.builder()
                .id(authorityResponse.getId())
                .authority(authorityResponse.getAuthority())
                .build();
    }

    @Override
    public Authority mapToAuthority(AuthorityRequest authorityRequest) {
        return Authority.builder()
                .authority(authorityRequest.getAuthority())
                .build();
    }

}

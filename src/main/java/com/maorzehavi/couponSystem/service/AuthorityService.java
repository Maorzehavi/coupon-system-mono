package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.AuthorityRequest;
import com.maorzehavi.couponSystem.model.dto.response.AuthorityResponse;
import com.maorzehavi.couponSystem.model.entity.Authority;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorityService {

    Boolean existsByAuthority(String authority);

    Optional<Long> getIdByAuthority(String authority);

    Optional<AuthorityResponse> getAuthority(Long id);

    Optional<Authority> getAuthorityEntity(Long id);

    Optional<AuthorityResponse> createAuthority(AuthorityRequest authority);

    Optional<AuthorityResponse> updateAuthority(Long id, AuthorityRequest authority);

    Optional<AuthorityResponse> deleteAuthority(Long id);


    List<AuthorityResponse> getAllAuthorities();


    Optional<AuthorityResponse> getByAuthority(String authority);

    Optional<List<AuthorityResponse>> getAllByAuthorityIn(Set<String> authorities);

    // mapping methods

    AuthorityResponse mapToAuthorityResponse(Authority authority);

    AuthorityResponse mapToAuthorityResponse(AuthorityRequest authorityRequest);

    Authority mapToAuthority(AuthorityResponse authorityResponse);

    Authority mapToAuthority(AuthorityRequest authorityRequest);

}

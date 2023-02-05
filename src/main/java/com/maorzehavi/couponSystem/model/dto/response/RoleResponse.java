package com.maorzehavi.couponSystem.model.dto.response;

import com.maorzehavi.couponSystem.model.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private Long id;
    private String role;
    private ClientType clientType;
    private Set<AuthorityResponse> authorities;
}
